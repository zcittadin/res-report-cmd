package com.estatica.servicos.resicolor.reports.app;

import com.estatica.servicos.resicolor.reports.builder.ProcessoReportCreator;
import com.estatica.servicos.resicolor.reports.dao.ProdutoDAO;
import com.estatica.servicos.resicolor.reports.model.Produto;
import com.estatica.servicos.resicolor.reports.util.PeriodFormatter;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ReportGenerator extends Application {

	private static ProdutoDAO dao = new ProdutoDAO();

	public static void main(String[] args) {

		String lote = args[0];
		String index = args[1];
		String folder;

		if ("6".equals(index)) {
			folder = "R1000";
		} else if ("7".equals(index)) {
			folder = "TQDA";
		} else {
			folder = "Reator" + index;
		}

		Produto produto = dao.findByLote(Integer.parseInt(lote));
		String path = "C://Resicolor_App_Trunk//Relatorios//Processo//" + folder + "//lote_" + lote + ".pdf";

		Task<Integer> reportTask = new Task<Integer>() {
			@Override
			protected Integer call() throws Exception {
				String periodo = PeriodFormatter.formatPeriod(produto.getDtInicial(), produto.getDtFinal());
				Double producao = new Double(0);
				String[] fields = periodo.split(":");
				Integer hours = Integer.parseInt(fields[0]);
				Integer minutes = Integer.parseInt(fields[1]);
				Integer passedMinutes = 0;
				if (hours > 0) {
					passedMinutes = hours * 60;
					passedMinutes = passedMinutes + minutes;
					producao = (Double.parseDouble(String.valueOf(produto.getQuantidade()).replace(",", "."))
							/ passedMinutes) * 60;
					String str = String.format("%1.2f", producao);
					producao = Double.valueOf(str.replace(",", "."));
					int result = ProcessoReportCreator.build(produto, path, periodo, str);
					int maximum = 20;
					for (int i = 0; i < maximum; i++) {
						updateProgress(i, maximum);
						return new Integer(result);
					}
				} else {
					int result = ProcessoReportCreator.build(produto, path, periodo, "0,000");
					return new Integer(result);
				}
				return null;
			}
		};

		reportTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				System.exit(1);
			}
		});

		reportTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erro");
				alert.setHeaderText("Houve uma falha na emissão do relatório.");
				alert.showAndWait();
				System.exit(0);
			}
		});
		Thread t = new Thread(reportTask);
		t.start();
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("Emitindo relatório de processo...");
	}
}
