package ufc.quixada.npi.afastamento.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ufc.quixada.npi.afastamento.model.Programa;
import ufc.quixada.npi.afastamento.model.StatusReserva;
import ufc.quixada.npi.afastamento.service.PeriodoService;
import ufc.quixada.npi.afastamento.service.RelatorioService;

@Controller
@RequestMapping("administracao/relatorioOld")
public class RelatorioControllerOld {

	@Inject
	private RelatorioService relatorioService;

	@Inject
	private PeriodoService periodoService;
	
	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
	public String getRelatorios(Model model) {
		model.addAttribute("status", StatusReserva.values());
		model.addAttribute("programa", Programa.values());
		return "admin/relatorios";
	}

	@RequestMapping(value = "/reservas", method = RequestMethod.POST)
	public void getRelatorioReservas(@RequestParam("anoInicio") Integer anoinicio, @RequestParam("semestreInicio") Integer semestreInicio,
			@RequestParam("anoTermino") Integer anoTermino, @RequestParam("semestreTermino") Integer semestreTermino,
			@RequestParam("programa") String programa, @RequestParam("status") String status, @RequestParam("professor") String professor,
			HttpServletResponse response, HttpServletRequest request) {
		Resource resource = new ClassPathResource("/relatorios/reservas.jasper");
		
		
		try {
			InputStream reportStream = resource.getInputStream();
			Map<String,Object> params=new HashMap<String,Object>();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			params.put("emissao", "Emitido em " + dateFormat.format(new Date()));
			params.put("periodo", anoinicio + "." + semestreInicio + " a " + anoTermino + "." + semestreTermino);
			params.put("professor", professor);
			params.put("status", status);
			params.put("programa", programa);
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
	        servletOutputStream.flush();

	        response.setContentType("application/pdf");
	        JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream, params, new JREmptyDataSource());

	        servletOutputStream.flush();
	        servletOutputStream.close();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
