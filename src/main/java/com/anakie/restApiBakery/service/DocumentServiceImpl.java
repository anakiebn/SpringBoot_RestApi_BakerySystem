package com.anakie.restApiBakery.service;


import com.anakie.restApiBakery.entity.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private ProductService productService;


    @Override
    public String invoice(Order order) {
        String invoicePath = "C:\\Users\\t\\Desktop\\Java Program\\mycreation\\RestApiBakery\\src\\main\\java\\com\\anakie\\restApiBakery\\slips\\order" + order.getId() + ".pdf";

        try {

            // Create a document with adjusted margins
            Document document = new Document(PageSize.A4, 20, 10,10,10);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(invoicePath));
            document.open();


            // Set background color
            PdfContentByte canvas = writer.getDirectContentUnder();
            Rectangle rect = new Rectangle(0, 0, PageSize.A4.getWidth(), PageSize.A4.getHeight());
//            rect.setBackgroundColor(new BaseColor(21, 31, 25)); // Blueish background
            rect.setBackgroundColor(BaseColor.WHITE);
            canvas.rectangle(rect);

            // Add paragraph
            StringBuilder msg = new StringBuilder();
            String name = Character.toUpperCase(order.getUser().getUsername().charAt(0)) + order.getUser().getUsername().substring(1);

            msg.append("Hello ").append(name)
                    .append("\n\nThanks for choosing us today, our bakers are preparing your order and it will be delivered soon!!");
            Font paragraphFont = new Font(Font.FontFamily   .HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            Paragraph paragraph = new Paragraph(msg.toString(), paragraphFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);


            // Adjust position by setting spacing before and after
            paragraph.setSpacingBefore(20f); // Set the space before the paragraph
            paragraph.setSpacingAfter(20f); // Set the space after the paragraph

            document.add(paragraph);

            // the name
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
            Paragraph title = new Paragraph("Order #" + order.getId(), titleFont);
            title.setAlignment(Element.ALIGN_LEFT);
            document.add(title);

            // the date
            Font titleFont2 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            Paragraph title2 = new Paragraph(order.getDateTime().getDayOfMonth()+" " + order.getDateTime().getMonth().toString() +" "+ order.getDateTime().getYear(), titleFont2);
            title2.setAlignment(Element.ALIGN_LEFT);
            document.add(title2);

            // Add table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(95);
            table.setSpacingBefore(20f);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorderColor(BaseColor.BLACK);

            String[] headers = {"Product", "Quantity", "Price per Item", "Total"};
            for (int i = 0; i < 4; i++) {
                PdfPCell cell = new PdfPCell(new Phrase(headers[i], new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setBorderColor(BaseColor.BLACK);
                cell.setNoWrap(false);
                cell.setPadding(5f);
                table.addCell(cell);
            }
            log.info("Shopping cart {}",order.getShoppingCart().getCartItems().toString());
            for (CartItem cartItem : order.getShoppingCart().getCartItems()) {
                Product product = productService.findById(cartItem.getProductId());
                log.info("accessing cart no: {}",cartItem.getId());
                int qty = cartItem.getProductQty();
                String firstColumn=product.getName()+"\n"+product.getWeight()+" "+product.getUnit();
                String[] content = new String[]{firstColumn, "x "+qty,"R "+ product.getPrice(),"R "+ product.getPrice() * qty};

                for (int i = 0; i < content.length; i++) {
                    log.info("Product info {}",content[0]);
                    PdfPCell cell = new PdfPCell(new Phrase(content[i], new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK)));
                    if (i == 0) {
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    } else {
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    }
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setBorderColor(BaseColor.BLACK);
                    cell.setNoWrap(false);
                    cell.setPadding(5f);
                    table.addCell(cell);
                }
            }
            document.add(table);


            // it's paragraph
            Paragraph totalParagraph = new Paragraph();
            Font totalParagraphFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            totalParagraph.setAlignment(Element.ALIGN_LEFT);

            // Adjust position by setting spacing before and after
            totalParagraph.setSpacingBefore(20f); // Set the space before the paragraph
            totalParagraph.setSpacingAfter(20f); // Set the space after the paragraph

            totalParagraph.add(new Chunk("Total : ",totalParagraphFont));
            for(int i=0;i<10;i++){
                totalParagraph.add(Chunk.TABBING); // Use multiple spaces or a fixed-width Chunk
            }
            totalParagraph.add(new Chunk("       R "+order.getTotalPrice(),  totalParagraphFont));
            document.add(totalParagraph);

            document.close();
            log.info("Custom PDF generated successfully!");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoicePath;
    }

}
