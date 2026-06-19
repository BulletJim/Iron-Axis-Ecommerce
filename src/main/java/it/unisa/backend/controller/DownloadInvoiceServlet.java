package it.unisa.backend.controller;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import it.unisa.backend.model.bean.OrderBean;
import it.unisa.backend.model.bean.OrderItemBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.dao.impl.OrderDAO;
import it.unisa.backend.model.db.DBManager;


@WebServlet("/DownloadInvoiceServlet")
public class DownloadInvoiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private OrderDAO orderDao;
	
	@Override
	public void init() throws ServletException {
		orderDao = new OrderDAO(DBManager.getDataSource());
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean loggedUser = (UserBean) request.getSession().getAttribute("loggedUser");
		if(loggedUser == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized, user must be logged in");
			return;
		}
		
		Long orderId = (Long) request.getSession().getAttribute("lastOrderId");
		
		// Fallback: if the request comes from user or admin requesting order history
        if (orderId == null) {
            String paramId = request.getParameter("orderId");
            if (paramId != null && !paramId.isEmpty()) {
                try {
                    orderId = Long.parseLong(paramId);
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Format Error");
                    return;
                }
            }
        }

        if (orderId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No Order Selected");
            return;
        }
		
        OrderBean order = orderDao.findById(orderId);

     // Controlla se l'ordine non esiste, oppure se l'utente loggato non è il proprietario dell'ordine e non è un admin
     if (order == null || (!order.getUser().getEmail().equals(loggedUser.getEmail()) && !"admin".equals(loggedUser.getRole()))) {
         response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden.");
         return;
     }
		
		if (order.getInvoice() == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invoice hasn't benn generated");
            return;
       }
		
		/*
		 * Colour and font initialization
		 * Global variables declared
		 * */
		Color primaryColour = new Color(229, 57, 53);   // --primary-color: #e53935
        Color darkBg = new Color(26, 26, 26);          // --dark-bg: #1a1a1a
        Color lightBg = new Color(248, 249, 250);      // --light-bg: #f8f9fa
        Color textLight = new Color(255, 255, 255);    // --text-light: #ffffff
        Color textDark = new Color(33, 37, 41);        // --text-dark: #212529
        Color borderColour = new Color(51, 51, 51);     // --border-color: #333333
        
        Font brandFont = new Font(Font.HELVETICA, 22, Font.BOLD, primaryColour);
        Font headerTextFont = new Font(Font.HELVETICA, 10, Font.NORMAL, textLight);
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, textDark);
        Font tableHeaderFont = new Font(Font.HELVETICA, 11, Font.BOLD, textLight);
        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL, textDark);
        Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD, textDark);
        Font totalFont = new Font(Font.HELVETICA, 14, Font.BOLD, primaryColour);
        
        /*
         * Initialized ByteArrrayOutputStram and Document objects
         * */
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document pdf = new Document(PageSize.A4, 0, 0, 0, 40);
		
		try {
			
			PdfWriter.getInstance(pdf, outputStream);
			pdf.open();
			
			// Brand And Info Header Section
			PdfPTable header = new PdfPTable(2);
			header.setWidthPercentage(100);
			
			PdfPCell brandCell = new PdfPCell();
			brandCell.setBackgroundColor(darkBg);
			brandCell.setBorder(PdfPCell.NO_BORDER);
            brandCell.setPadding(20f);
            brandCell.setPaddingLeft(40f);
            brandCell.addElement(new Paragraph("IRON AXIS", brandFont));
            brandCell.addElement(new Paragraph("Integratori e Attrezzature Sportive", headerTextFont));
            header.addCell(brandCell);
            
            PdfPCell infoCell = new PdfPCell();
            infoCell.setBackgroundColor(darkBg);
            infoCell.setBorder(PdfPCell.NO_BORDER);
            infoCell.setPadding(20f);
            infoCell.setPaddingRight(40f);
            Paragraph pIva = new Paragraph("Via Martiri Ungheresi 12, 84128 Salerno (SA)\nP.IVA: 01234567890", headerTextFont);
            pIva.setAlignment(Element.ALIGN_RIGHT);
            infoCell.addElement(pIva);
            header.addCell(infoCell);
            
            pdf.add(header);
			
            // Invisible table used to add margins
            PdfPTable contentTable = new PdfPTable(1);
            contentTable.setWidthPercentage(85); 
            PdfPCell contentWrapper = new PdfPCell();
            contentWrapper.setBorder(PdfPCell.NO_BORDER);
            contentWrapper.setPaddingTop(30f);

            // Customer and Invoice info
            PdfPTable customerInvoiceInfoTable = new PdfPTable(2);
            customerInvoiceInfoTable.setWidthPercentage(100);

            // Customer (On the left)
            PdfPCell customerCell = new PdfPCell();
            customerCell.setBorder(PdfPCell.NO_BORDER);
            customerCell.addElement(new Paragraph("Intestato a:", boldFont));
            customerCell.addElement(new Paragraph(order.getInvoice().getHolderFirstName() + " " + order.getInvoice().getHolderLastName(), normalFont));
            String address = order.getShippingAddress().getStreet() + " " + order.getShippingAddress().getStreetNumber();
            String city = order.getShippingAddress().getZipCode() + " " + order.getShippingAddress().getCity() + " (" + order.getShippingAddress().getProvince() + ")";
            String country = order.getShippingAddress().getCountry().toUpperCase();
            customerCell.addElement(new Paragraph(address, normalFont));
            customerCell.addElement(new Paragraph(city, normalFont));
            customerCell.addElement(new Paragraph(country, normalFont));
            customerCell.addElement(new Paragraph(loggedUser.getEmail(), normalFont));

            customerInvoiceInfoTable.addCell(customerCell);

            // Invoice (On the right)
            PdfPCell invoiceCell = new PdfPCell();
            invoiceCell.setBorder(PdfPCell.NO_BORDER);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataStr = order.getInvoice().getIssueDate().format(formatter);
            
            Paragraph invoiceTitle = new Paragraph("FATTURA", titleFont);
            invoiceTitle.setAlignment(Element.ALIGN_RIGHT);
            invoiceCell.addElement(invoiceTitle);
            
            Paragraph invoiceDetails = new Paragraph("N° Documento: " + order.getInvoice().getNumber() + "\nData: " + dataStr, normalFont);
            invoiceDetails.setAlignment(Element.ALIGN_RIGHT);
            invoiceCell.addElement(invoiceDetails);
            customerInvoiceInfoTable.addCell(invoiceCell);

            contentWrapper.addElement(customerInvoiceInfoTable);
            contentWrapper.addElement(new Paragraph("\n\n"));

            // Products Table
            PdfPTable itemsInfoTable = new PdfPTable(new float[]{4, 1, 2, 2});
            itemsInfoTable.setWidthPercentage(100);

            // Product Header
            addCell(itemsInfoTable, "Prodotto", tableHeaderFont, darkBg, Element.ALIGN_LEFT, borderColour);
            addCell(itemsInfoTable, "Q.tà", tableHeaderFont, darkBg, Element.ALIGN_CENTER, borderColour);
            addCell(itemsInfoTable, "Prezzo Unitario", tableHeaderFont, darkBg, Element.ALIGN_RIGHT, borderColour);
            addCell(itemsInfoTable, "Totale", tableHeaderFont, darkBg, Element.ALIGN_RIGHT, borderColour);

            // Takes all OrderItems
            for (OrderItemBean item : order.getItems()) {
                String productName = "Articolo SKU: " + item.getVariant().getSku() + " (" + item.getVariant().getFlavour() + ")"; 
                
                addCell(itemsInfoTable, productName, normalFont, lightBg, Element.ALIGN_LEFT, borderColour);
                addCell(itemsInfoTable, String.valueOf(item.getQuantity()), normalFont, lightBg, Element.ALIGN_CENTER, borderColour);
                addCell(itemsInfoTable, String.format("%.2f €", item.getPriceAtPurchase()), normalFont, lightBg, Element.ALIGN_RIGHT, borderColour);
                
                double rowTotal = item.getPriceAtPurchase() * item.getQuantity();
                addCell(itemsInfoTable, String.format(" %.2f €", rowTotal), normalFont, lightBg, Element.ALIGN_RIGHT, borderColour);
            }
            contentWrapper.addElement(itemsInfoTable);
            contentWrapper.addElement(new Paragraph("\n"));

            // Accounting Info
            PdfPTable accountingInfoTable = new PdfPTable(new float[]{3, 1});
            accountingInfoTable.setWidthPercentage(50); 
            accountingInfoTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            double taxable = order.getInvoice().getTaxableAmount();
            double total = order.getInvoice().getTotalAmount();
            double vat = total - taxable; 
            double shippingCost = order.getShippingCosts();

            addNoBorderCell(accountingInfoTable, "Imponibile:", normalFont, Element.ALIGN_RIGHT);
            addNoBorderCell(accountingInfoTable, String.format("%.2f €", taxable), normalFont, Element.ALIGN_RIGHT);

            addNoBorderCell(accountingInfoTable, "Totale IVA:", normalFont, Element.ALIGN_RIGHT);
            addNoBorderCell(accountingInfoTable, String.format("%.2f €", vat), normalFont, Element.ALIGN_RIGHT);
            
            addNoBorderCell(accountingInfoTable, "Spedizione:", normalFont, Element.ALIGN_RIGHT);
            addNoBorderCell(accountingInfoTable, String.format("%.2f €", shippingCost), normalFont, Element.ALIGN_RIGHT);

            // Total Row
            PdfPCell totalLabel = new PdfPCell(new Phrase("TOTALE:", totalFont));
            totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalLabel.setBorder(PdfPCell.TOP);
            totalLabel.setBorderColorTop(primaryColour);
            totalLabel.setBorderWidthTop(2f);
            totalLabel.setPaddingTop(10f);
            accountingInfoTable.addCell(totalLabel);

            PdfPCell totalValue = new PdfPCell(new Phrase(String.format("%.2f €", total), totalFont));
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalValue.setBorder(PdfPCell.TOP);
            totalValue.setBorderColorTop(primaryColour);
            totalValue.setBorderWidthTop(2f);
            totalValue.setPaddingTop(10f);
            accountingInfoTable.addCell(totalValue);
            

            contentWrapper.addElement(accountingInfoTable);
            
            contentWrapper.addElement(new Paragraph("\n\n"));
            
            PdfPTable paymentInfoTable = new PdfPTable(1);
            paymentInfoTable.setWidthPercentage(100);
            
            // Payment Header
            PdfPCell paymentHeader = new PdfPCell(new Phrase("Dettagli Pagamento", boldFont));
            paymentHeader.setBorder(PdfPCell.BOTTOM);
            paymentHeader.setBorderColorBottom(borderColour);
            paymentHeader.setBorderWidthBottom(1f);
            paymentHeader.setPaddingBottom(5f);
            paymentInfoTable.addCell(paymentHeader);
            
            // Payment Details
            PdfPCell paymentDetails = new PdfPCell();
            paymentDetails.setBorder(PdfPCell.NO_BORDER);
            paymentDetails.setPaddingTop(8f);
            
            String pMethod = order.getPayment().getPaymentMethod() != null ? order.getPayment().getPaymentMethod() : "N/A";
            String cardCircuit = order.getPayment().getCardCircuit() != null ? order.getPayment().getCardCircuit() : "N/A";
            String lastFourDigits = order.getPayment().getLastFourDigits() != null ? order.getPayment().getLastFourDigits() : "N/A";
            String pTransId = order.getPayment().getTransactionId() != null ? order.getPayment().getTransactionId() : "N/A";
            String pStatus = order.getPayment().getStatus() != null ? order.getPayment().getStatus().toString() : "N/A";
            String paymentDate = order.getPayment().getPaymentDate() != null ? DateTimeFormatter.ofPattern("dd/MM/yyyy")
            		.format(order.getPayment().getPaymentDate()) : "N/A";
            
            paymentDetails.addElement(new Paragraph("Metodo di pagamento: " + pMethod + " - " + cardCircuit + " ****" + lastFourDigits, normalFont));

            paymentDetails.addElement(new Paragraph("ID Transazione: " + pTransId, normalFont));
            paymentDetails.addElement(new Paragraph("Stato del pagamento: " + pStatus, normalFont));
            paymentDetails.addElement(new Paragraph("Data di esecuzione: " + paymentDate, normalFont));
            
            paymentInfoTable.addCell(paymentDetails);
            contentWrapper.addElement(paymentInfoTable);
            
            
            // Add Wrapper element to document
            contentTable.addCell(contentWrapper);
            pdf.add(contentTable);

        } catch (DocumentException e) {
            throw new ServletException("PDF formatting error", e);
        } finally {
            if (pdf != null && pdf.isOpen()) {
                pdf.close();
            }
        }
			
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=" + order.getInvoice().getNumber() + ".pdf");		
			response.setContentLength(outputStream.size());
			
			response.getOutputStream().write(outputStream.toByteArray());
			response.getOutputStream().flush();
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
	}
	
	private void addCell(PdfPTable table, String testo, Font font, Color bgColor, int alignment, Color borderColor) {
        PdfPCell cell = new PdfPCell(new Phrase(testo, font));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(10f);
        cell.setBorderColor(borderColor);
        table.addCell(cell);
    }

    private void addNoBorderCell(PdfPTable table, String testo, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(testo, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPaddingTop(6f);
        cell.setPaddingBottom(6f);
        table.addCell(cell);
    }

}
