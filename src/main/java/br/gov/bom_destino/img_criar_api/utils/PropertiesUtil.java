package br.gov.bom_destino.img_criar_api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
	
	public static String obterURI(String nomeServico) throws IOException {
		final File services = new File(System.getProperty("jboss.home.dir") + "/properties/services.properties");
		
		Properties properties = new Properties();
		
		FileInputStream fis = new FileInputStream(services);
		
		properties.load(new InputStreamReader(fis));
		
		fis.close();
		
		return properties.get(nomeServico).toString();
	}
}
