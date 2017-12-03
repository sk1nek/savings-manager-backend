package me.mjaroszewicz.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

public abstract class AbstractITextPdfView extends AbstractView {

    public AbstractITextPdfView(){
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent(){
        return true;
    }

    @Override
    protected void renderMergedOutputModel(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{

        ByteArrayOutputStream baos = createTemporaryOutputStream();

        Document document = new Document();
        PdfWriter writer = newWriter(document, baos);
        prepareWriter(model, writer, request);

        document.open();
        buildPdfDocument(model, document, writer, request, response);
        document.close();

        writeToResponse(response, baos);

    }

    protected PdfWriter newWriter(Document doc, OutputStream os) throws DocumentException{
        return PdfWriter.getInstance(doc, os);
    }

    protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request) throws DocumentException{

        writer.setViewerPreferences(getViewerPreferences());
    }

    protected int getViewerPreferences(){
        return PdfWriter.ALLOW_PRINTING;
    }

    protected abstract void buildPdfDocument(Map<String,Object> model, Document doc, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception;


}
