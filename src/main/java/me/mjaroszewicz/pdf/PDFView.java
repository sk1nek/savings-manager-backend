package me.mjaroszewicz.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import me.mjaroszewicz.entities.BalanceChange;
import me.mjaroszewicz.exceptions.PdfBuildingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

public class PDFView extends AbstractITextPdfView {

    @SuppressWarnings("unchecked")
    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document doc,
                                    PdfWriter writer,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        ArrayList<BalanceChange> itemList;

        if((itemList = (ArrayList<BalanceChange>) model.get("items")) == null)
            throw new PdfBuildingException("Incorrectly passed list of items. (probably nullpointer?)");

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100.0f);
        table.setSpacingBefore(15);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.GRAY);
        cell.setPadding(5);

        cell.setPhrase(new Phrase("title"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("details"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("amount"));
        table.addCell(cell);

        itemList.forEach(e-> {
            table.addCell(e.getTitle());
            table.addCell(e.getDetails());

            if(e.isExpense())
                table.addCell("-" + e.getAmount());
            else
                table.addCell("" + e.getAmount());
        });

        doc.add(table);
    }
}
