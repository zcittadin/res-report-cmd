package com.estatica.servicos.resicolor.reports.util;

import com.estatica.servicos.resicolor.reports.model.Processo;
import com.estatica.servicos.resicolor.reports.model.Produto;

public class ProducaoUtil {

	public static double getTempMin(Produto produto) {
		double min = 1000;
		for (Processo processo : produto.getProcessos()) {
			if (processo.getTempReator() < min) {
				min = processo.getTempReator();
			}
		}
		return min;
	}

	public static double getTempMax(Produto produto) {
		double max = 0;
		for (Processo processo : produto.getProcessos()) {
			if (processo.getTempReator() >= max) {
				max = processo.getTempReator();
			}
		}
		return max;
	}
}
