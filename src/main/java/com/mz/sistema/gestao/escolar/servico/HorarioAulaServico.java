package com.mz.sistema.gestao.escolar.servico;

import java.util.Date;
import java.util.List;

import com.mz.sistema.gestao.escolar.modelo.HorarioAula;

public interface HorarioAulaServico {
	
	public void salvar(HorarioAula horarioAula);
	public void excluir(HorarioAula horarioAula);
	public List<HorarioAula> obterPorTurno(Long idTurno);
	public HorarioAula horarioAulaExistenteDaEscola(Long idTurno, Date horaInicial, Integer ordem);
	public List<HorarioAula> obterHorarioAulaPorEscolaTurno(Long idTurno);
	public List<HorarioAula>  obterHorarioAulaPorIdEscolaPoridTurno(Long idTurno, Long idEscola);
	public HorarioAula obterHorarioAulaPorId(Long idHorarioAula);

}
