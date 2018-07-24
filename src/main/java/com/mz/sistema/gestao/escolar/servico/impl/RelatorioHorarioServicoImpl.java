package com.mz.sistema.gestao.escolar.servico.impl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*
 * 
 * 
 * 
 * Autor do sistema Agostinho Bartolomeu jolocholo
 * 
 * 
 * 
 * */
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mz.sistema.gestao.escolar.modelo.Horario;
import com.mz.sistema.gestao.escolar.modelo.HorarioAula;
import com.mz.sistema.gestao.escolar.modelo.ProfessorTurma;
import com.mz.sistema.gestao.escolar.modelo.Turma;
import com.mz.sistema.gestao.escolar.servico.HorarioAulaServico;
import com.mz.sistema.gestao.escolar.servico.HorarioServico;
import com.mz.sistema.gestao.escolar.servico.ProfessorTurmaServico;
import com.mz.sistema.gestao.escolar.servico.RelatorioHorarioServico;
import com.mz.sistema.gestao.escolar.util.DataUtils;

@Service
@Transactional
public class RelatorioHorarioServicoImpl implements RelatorioHorarioServico {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private HorarioServico horarioServico;
	@Autowired
	private HorarioAulaServico horarioAulaServico;

	@Autowired
	private ProfessorTurmaServico professorTurmaServico;

	private static String FORMATO_HORA = "HH:mm";
	private static String FORMATO_DIA_HORA = "HH:mm dd-MM-yyyy";

	private String discipinaSEG = "";
	private String discipinaTER = "";
	private String discipinaQUA = "";
	private String discipinaQUI = "";
	private String discipinaSEX = "";

	@Override
	public void desenharRelatorioAluno(Turma turma) {
		try {
			List<Horario> horarios = new ArrayList<>();
			List<HorarioAula> horarioAulas = new ArrayList<>();
			List<ProfessorTurma> professorTurmas = new ArrayList<>();
			horarioAulas = horarioAulaServico.obterHorarioAulaPorIdEscolaPoridTurno(turma.getTurno().getId(),
					turma.getEscola().getId());
			horarios = horarioServico.obterHorarioIdTurma(turma.getId());
			Document document = new Document();
			Integer larguraDaPagina = 560;
			String titulo = "horario da minha turma.pdf";
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			ServletOutputStream servletOutputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment;filename=" + titulo);
			context.responseComplete();
			ByteArrayOutputStream boas = new ByteArrayOutputStream();
			Font font = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD, BaseColor.BLACK);
			Font fontNormal = FontFactory.getFont(FontFactory.COURIER, 10, Font.NORMAL, BaseColor.BLACK);
			ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
			String caminhoImagem = "/resources/images/emblema.png";

			PdfWriter.getInstance(document, boas);
			document.open();

			Rectangle rectangle = new Rectangle(PageSize.A4);
			document.add(rectangle);
			document.setMargins(15, 40, 15, 15);
			document.setMarginMirroring(false);
			String caminhoReal = servletContext.getRealPath(caminhoImagem);
			Image image = Image.getInstance(caminhoReal);
			image.setAlignment(1);
			image.scaleToFit(30, 30);
			document.add(image);
			Paragraph p = new Paragraph("REPÚBLICA DE MOÇAMBIQUE", font);
			p.setAlignment(1);
			document.add(p);
			p = new Paragraph("PROVINCIA DE " + horarioAulas.get(0).getEscola().getDistrital().getEndereco()
					.getDistrito().getProvincia().toUpperCase(), font);
			p.setAlignment(1);
			document.add(p);
			p = new Paragraph("GOVERNO DO DISTRITO DE " + horarioAulas.get(0).getEscola().getDistrital().getEndereco()
					.getDistrito().getNome().toUpperCase(), font);
			p.setAlignment(1);
			document.add(p);

			PdfPTable pdfPTable1 = new PdfPTable(1);
			p = new Paragraph(horarioAulas.get(0).getEscola().getDescricao().toUpperCase() + "", font);
			p.setAlignment(1);
			PdfPCell cell = new PdfPCell(p);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPaddingBottom(5);
			cell.setBorder(0);
			cell.setBorderWidthBottom((float) 0.30);
			pdfPTable1.addCell(cell);
			pdfPTable1.setSpacingAfter(3);
			pdfPTable1.setTotalWidth(larguraDaPagina);
			pdfPTable1.setLockedWidth(true);

			document.add(pdfPTable1);
			PdfPTable pdfPTable = new PdfPTable(1);

			p = new Paragraph("HORÁRIO DA MINHA TURMA", font);
			cell = new PdfPCell(p);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPaddingBottom(5);
			cell.setPaddingBottom(5);
			pdfPTable.addCell(cell);
			pdfPTable.setTotalWidth(larguraDaPagina);
			pdfPTable.setLockedWidth(true);
			document.add(pdfPTable);

			p = new Paragraph(" ");
			document.add(p);
			PdfPTable pTable = new PdfPTable(new float[] { 38, 200, 120, 80 });

			p = new Paragraph("Classe: ", fontNormal);
			cell = new PdfPCell(p);

			cell.setBorder(0);
			pTable.addCell(cell);
			p = new Paragraph(turma.getClasse().getDescricao(), font);
			cell = new PdfPCell(p);
			cell.setPaddingTop(5);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

			cell.setBorder(0);
			pTable.addCell(cell);

			cell = new PdfPCell();
			cell.setBorder(0);
			pTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBorder(0);
			pTable.addCell(cell);

			p = new Paragraph("Turno: ", fontNormal);
			cell = new PdfPCell(p);
			cell.setBorder(0);
			pTable.addCell(cell);
			p = new Paragraph(turma.getTurno().getDescricao().getLabel(), font);
			cell = new PdfPCell(p);
			cell.setPaddingTop(5);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

			cell.setBorder(0);
			pTable.addCell(cell);

			p = new Paragraph("Emitido em: ", fontNormal);
			cell = new PdfPCell(p);
			cell.setHorizontalAlignment(2);
			cell.setBorder(0);
			pTable.addCell(cell);
			String dia_hora = DataUtils.obterDataFormatoBanco(new Date(), FORMATO_DIA_HORA);
			p = new Paragraph(dia_hora, font);
			cell = new PdfPCell(p);
			cell.setPaddingTop(5);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

			cell.setBorder(0);
			pTable.addCell(cell);

			p = new Paragraph("Turma: ", fontNormal);
			cell = new PdfPCell(p);
			cell.setBorder(0);
			pTable.addCell(cell);
			p = new Paragraph(turma.getDescricao(), font);
			cell = new PdfPCell(p);
			cell.setPaddingTop(5);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

			cell.setBorder(0);
			pTable.addCell(cell);

			cell = new PdfPCell();
			cell.setBorder(0);
			pTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBorder(0);
			pTable.addCell(cell);

			pTable.setTotalWidth(larguraDaPagina);
			pTable.setLockedWidth(true);
			document.add(pTable);
			p = new Paragraph(" ");
			document.add(p);

			font = FontFactory.getFont(FontFactory.COURIER, 9, Font.BOLD, BaseColor.BLACK);

			PdfPTable table = new PdfPTable(6);

			PdfPCell cell1 = new PdfPCell(new Paragraph("Horários", font));
			cell1.setHorizontalAlignment(1);
			cell1.setPaddingBottom(5);
			table.addCell(cell1);

			PdfPCell cell2 = new PdfPCell(new Paragraph("Segunda", font));
			cell2.setHorizontalAlignment(1);
			cell2.setPaddingBottom(5);
			table.addCell(cell2);

			PdfPCell cell3 = new PdfPCell(new Paragraph("Terça", font));
			cell3.setHorizontalAlignment(1);
			cell3.setPaddingBottom(5);
			table.addCell(cell3);

			PdfPCell cell4 = new PdfPCell(new Paragraph("Quarta", font));
			cell4.setHorizontalAlignment(1);
			cell4.setPaddingBottom(5);
			table.addCell(cell4);

			PdfPCell cell5 = new PdfPCell(new Paragraph("Quinta", font));
			cell5.setHorizontalAlignment(1);
			cell5.setPaddingBottom(5);
			table.addCell(cell5);

			PdfPCell cell6 = new PdfPCell(new Paragraph("Sexta", font));
			cell6.setHorizontalAlignment(1);
			cell6.setPaddingBottom(5);
			table.addCell(cell6);

			fontNormal = FontFactory.getFont(FontFactory.COURIER, 9, Font.NORMAL, BaseColor.BLACK);

			for (HorarioAula horarioAula : horarioAulas) {
				cell1 = new PdfPCell(new Paragraph(
						DataUtils.obterDataFormatoBanco(horarioAula.getAulaInicial(), FORMATO_HORA) + " - "
								+ DataUtils.obterDataFormatoBanco(horarioAula.getAulaFinal(), FORMATO_HORA) + "",
						fontNormal));
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.setPaddingBottom(5);
				discipinaSEG = "---";
				discipinaTER = "---";
				discipinaQUA = "---";
				discipinaQUI = "---";
				discipinaSEX = "---";
				verificarHorario(horarios, horarioAula);
				cell2 = new PdfPCell(new Paragraph(discipinaSEG, fontNormal));
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell2.setPaddingBottom(5);

				cell3 = new PdfPCell(new Paragraph(discipinaTER, fontNormal));
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell3.setPaddingBottom(5);

				cell4 = new PdfPCell(new Paragraph(discipinaQUA, fontNormal));
				cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell4.setPaddingBottom(5);

				cell5 = new PdfPCell(new Paragraph(discipinaQUI, fontNormal));
				cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell5.setPaddingBottom(5);

				cell6 = new PdfPCell(new Paragraph(discipinaSEX, fontNormal));
				cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell6.setPaddingBottom(5);

				table.addCell(cell1);
				table.addCell(cell2);
				table.addCell(cell3);
				table.addCell(cell4);
				table.addCell(cell5);
				table.addCell(cell6);

			}

			professorTurmas = professorTurmaServico.obterProfessorTurmaPorTurmaOrdernarPorNome(turma.getId());

			table.setTotalWidth(larguraDaPagina);
			table.setLockedWidth(true);
			document.add(table);

			p = new Paragraph(" ");
			document.add(p);

			table = new PdfPTable(new float[] { 35, 230, 185, 100 });

			PdfPCell cell10 = new PdfPCell(new Paragraph("Ord.", font));
			cell10.setHorizontalAlignment(1);
			cell10.setPaddingBottom(5);
			table.addCell(cell10);
			PdfPCell cell7 = new PdfPCell(new Paragraph("Professor", font));
			cell7.setHorizontalAlignment(1);
			cell7.setPaddingBottom(5);
			table.addCell(cell7);

			PdfPCell cell8 = new PdfPCell(new Paragraph("Disciplina", font));
			cell8.setHorizontalAlignment(1);
			cell8.setPaddingBottom(5);
			table.addCell(cell8);
			PdfPCell cell9 = new PdfPCell(new Paragraph("C.H. Semanal", font));
			cell9.setHorizontalAlignment(1);
			cell9.setPaddingBottom(5);
			table.addCell(cell9);

			disciplinLecionadaPorProfessor(professorTurmas, fontNormal, table);

			table.setTotalWidth(larguraDaPagina);
			table.setLockedWidth(true);
			document.add(table);

			document.close();

			byte[] bytes = boas.toByteArray();
			servletOutputStream.write(bytes, 0, bytes.length);

			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void disciplinLecionadaPorProfessor(List<ProfessorTurma> professorTurmas, Font fontNormal,
			PdfPTable table) {
		PdfPCell cell7, cell8, cell9, cell10;

		Integer count = 0;
		String countador = "";
		for (ProfessorTurma professorTurma : professorTurmas) {
			count++;
			if (count < 10) {
				countador = "0" + count;
			} else {
				countador = count + "";
			}
			cell10 = new PdfPCell(new Paragraph(countador + "", fontNormal));
			cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell10.setPaddingBottom(5);

			cell7 = new PdfPCell(new Paragraph(professorTurma.getProfessor().getNome() + "", fontNormal));
			cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell7.setPaddingBottom(5);

			cell8 = new PdfPCell(new Paragraph(professorTurma.getDisciplina().getDescricao() + "", fontNormal));
			cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell8.setPaddingBottom(5);

			cell9 = new PdfPCell(new Paragraph(professorTurma.getCredito() + "", fontNormal));
			cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell9.setPaddingBottom(5);

			table.addCell(cell10);
			table.addCell(cell7);
			table.addCell(cell8);
			table.addCell(cell9);
		}
	}

	private void verificarHorario(List<Horario> horarios, HorarioAula horarioAula) {
		for (Horario horario : horarios) {
			if (horario.getDiaSemana().getSigla().equals("SEG")
					&& horario.getHorarioAula().getId() == horarioAula.getId()) {
				discipinaSEG = horario.getDisciplina().getDescricao();
			}
			if (horario.getDiaSemana().getSigla().equals("TER")
					&& horario.getHorarioAula().getId() == horarioAula.getId()) {
				discipinaTER = horario.getDisciplina().getDescricao();
			}
			if (horario.getDiaSemana().getSigla().equals("QUA")
					&& horario.getHorarioAula().getId() == horarioAula.getId()) {
				discipinaQUA = horario.getDisciplina().getDescricao();
			}
			if (horario.getDiaSemana().getSigla().equals("QUI")
					&& horario.getHorarioAula().getId() == horarioAula.getId()) {
				discipinaQUI = horario.getDisciplina().getDescricao();

			}
			if (horario.getDiaSemana().getSigla().equals("SEX")
					&& horario.getHorarioAula().getId() == horarioAula.getId()) {

				discipinaSEX = horario.getDisciplina().getDescricao();
			}
		}
	}

}
