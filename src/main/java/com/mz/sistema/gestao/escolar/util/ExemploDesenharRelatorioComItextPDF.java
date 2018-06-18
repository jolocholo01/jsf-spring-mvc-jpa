package com.mz.sistema.gestao.escolar.util;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mz.sistema.gestao.escolar.enumerado.Provincia;
import com.mz.sistema.gestao.escolar.modelo.Distrito;

import net.sf.jasperreports.engine.JasperRunManager;

public class ExemploDesenharRelatorioComItextPDF {
	public static void main(String[] args) {
		Distrito distrito = new Distrito();
		distrito.setNome("Nampula");
		distrito.setProvincia("Nampula");
		geradorPDF(distrito);
	}

	public static void geradorPDF(Distrito distrito) {
		// Document doc = new Document(PageSize.A4.rotate(), 0,0,0,0);
		Document doc = new Document();
		Integer larguraPagina = 560;
		String arquivoPdf = "horario da minha turma.pdf";
		// File file =new File(arquivoPdf);
		// file.getParentFile().mkdirs();
		// new IndentationInCell

		try {
		/*	FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			ServletOutputStream servletOutputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment;filename=" + arquivoPdf);
			context.responseComplete();
*/
			Distrito distrito1 = new Distrito(1L, "Nacala porto", Provincia.NAMPULA.getLabel().toString());
			Distrito distrito2 = new Distrito(2L, "Ilh de Mocambique", Provincia.NAMPULA.getLabel().toString());
			Distrito distrito3 = new Distrito(3L, "Munapo", Provincia.NAMPULA.getLabel().toString());
			Distrito distrito4 = new Distrito(4L, "Nacala a velha", Provincia.NAMPULA.getLabel().toString());
			List<Distrito> distritos = new ArrayList<>();
			distritos.add(distrito1);
			distritos.add(distrito2);
			distritos.add(distrito3);
			distritos.add(distrito4);

			ByteArrayOutputStream boas = new ByteArrayOutputStream();

			Font fonte = FontFactory.getFont(FontFactory.COURIER, 10, Font.BOLD, BaseColor.BLACK);
			PdfWriter.getInstance(doc,  new FileOutputStream(arquivoPdf));
			doc.open();
			// InputStream is=new ByteArrayInputStream(arquivoPdf.getBytes());

			Rectangle rectangle = new Rectangle(PageSize.A4_LANDSCAPE);
			rectangle.setLeft(0);
			rectangle.setRight(0);
			rectangle.setTop(0);
			rectangle.setBottom(0);
			doc.add(rectangle);
			doc.setMargins(15, 15, 15, 15);
			doc.setMarginMirroring(false);
			// doc.newPage();

			// Cabacadlho do meu oc
			Paragraph p = new Paragraph("REPÚBLICA DE MOÇAMBIQUE", fonte);
			p.setAlignment(1);
			doc.add(p);
			p = new Paragraph("PROVÍNCIA DE " + distrito.getProvincia().toUpperCase(), fonte);
			p.setAlignment(1);
			doc.add(p);
			p = new Paragraph("GOVERNO DO DISTRITO DE " + distrito.getNome().toUpperCase(), fonte);
			p.setAlignment(1);
			doc.add(p);
			PdfPTable table1 = new PdfPTable(1);
			p = new Paragraph("ESCOLA SECUNDÁRIA DE NAMPULA", fonte);
			PdfPCell cell = new PdfPCell(new Paragraph(p));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.setSpacingBefore(1);
			cell.setPaddingBottom(5);
			cell.setBorder(0);
			// cell.set
			// cell.lin
			// table1.setFooterRows(1);
			table1.addCell(cell);
			// doc.add(Chunk.NEWLINE);

			cell = new PdfPCell(new Paragraph("HORÁRIO DA MINHA TURMA", fonte));
			cell.setPaddingBottom(7);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			// cell.setTop(100);
			table1.setPaddingTop(20);
			table1.setTotalWidth(larguraPagina);
			table1.setLockedWidth(true);
			table1.addCell(cell);
			doc.add(table1);

			p = new Paragraph("  ");
			doc.add(p);
			PdfPTable table = new PdfPTable(3);

			table.setWidths(new float[] { 10, 50, 80 });
			// p = new Paragraph();
			fonte = new Font();
			fonte = FontFactory.getFont(FontFactory.COURIER, 9, Font.BOLD, BaseColor.BLACK);

			PdfPCell cell1 = new PdfPCell(new Paragraph("#", fonte));

			// cell1.setto
			PdfPCell cell2 = new PdfPCell(new Paragraph("Distrito", fonte));
			PdfPCell cell3 = new PdfPCell(new Paragraph("Provincia", fonte));

			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

			table.addCell(cell1);
			table.addCell(cell2);
			table.addCell(cell3);
			fonte = new Font();
			fonte = FontFactory.getFont(FontFactory.COURIER, 9, Font.NORMAL, BaseColor.BLACK);

			for (Distrito distrito5 : distritos) {
				cell1 = new PdfPCell(new Paragraph(distrito5.getId() + "", fonte));
				cell2 = new PdfPCell(new Paragraph(distrito5.getNome(), fonte));
				cell3 = new PdfPCell(new Paragraph(distrito5.getProvincia(), fonte));

				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

				//table.addCell(cell1);
				table.addCell(cell2);
				table.addCell(cell3);
			}
			table.setTotalWidth(larguraPagina);
			table.setLockedWidth(true);
			table.addCell(cell);
			doc.add(table);

			doc.close();
			// boas.writeTo(new FileOutputStream(new File(arquivoPdf)));
//			byte[] bytes = boas.toByteArray();
//			servletOutputStream.write(bytes, 0, bytes.length);
//
//			servletOutputStream.flush();
//			servletOutputStream.close();
//			System.out.println("Pdf em byte:" + bytes.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
