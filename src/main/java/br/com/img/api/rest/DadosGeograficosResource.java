package br.com.img.api.rest;

import java.io.IOException;
import java.io.Serializable;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import br.com.img.api.utils.PropertiesUtil;

@Path("dados-geograficos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DadosGeograficosResource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ResteasyClient client;
	
	@POST
	public Response criarDadosGeograficos() throws IllegalArgumentException, NullPointerException, IOException {
		client = new ResteasyClientBuilder().build();
		
		WebTarget target = client.target(PropertiesUtil.obterURI("gateway-api")).path("dados-geograficos-ibge");
		
		target = target.queryParam("nome-cidade", "Bom Destino");
		target = target.queryParam("nome-api", "img-criar-api");
		
		Response response = target.request().get();
		
		String resposta = "";
		
		if(Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
			resposta = response.readEntity(String.class);
			
			System.out.println("ibge-api: " + resposta);
		}
		
		target = client.target(PropertiesUtil.obterURI("gateway-api")).path("mensageria-ibge").queryParam("nome-api", "img-criar-api");
		target.request().post(Entity.entity(resposta, MediaType.APPLICATION_JSON));
		
		target = client.target(PropertiesUtil.obterURI("gateway-api")).path("dados-geograficos-satelite").queryParam("nome-api", "img-criar-api");
		
		response = target.request().get();

		byte[] imagem = null;
		
		if(Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
			imagem = response.readEntity(byte[].class);
		}
		
		target = client.target(PropertiesUtil.obterURI("gateway-api")).path("mensageria-satelite").queryParam("nome-api", "img-criar-api");
		target.request().post(Entity.entity(imagem, MediaType.APPLICATION_JSON));
		
		return Response.ok().build();
	}
}