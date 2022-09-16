package com.essalud.gcpp.service.personal.imp;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.essalud.gcpp.dto.personal.BoletaPagoDTO;

public class CargarBoletaPago  {

	
	
	private final static String DIRECTORIO_UPLOAD = "data/boletapago";
	//private final static String DIRECTORIO_UPLOAD = "E:\\vacaciones\\boletapago";

	
	   public List<BoletaPagoDTO> parsePDFDocument(MultipartFile file) {
	     List<BoletaPagoDTO> boletas = new ArrayList<>(); 
	        File archivo;
	        PDDocument pdDocument = null;
	       
	        try {
	            pdDocument = PDDocument.load(file.getBytes());
	            String anno = "",mes = "";
	            for (int i = 1; i < pdDocument.getNumberOfPages()+2; i++) {
	            	
	            	PDFTextStripper reader = new PDFTextStripper();
	            	reader.setStartPage(i);
	            	reader.setEndPage(i);
	            	String pageText = reader.getText(pdDocument);
		            String[] lines = pageText.split("\r\n|\r|\n"); 
		            for (int j = 0; j < lines.length; j++) {
		            	if(i==1&& j==10) {
		            		anno = lines[j].split(" ")[1];
			            	mes=lines[j].split(" ")[0];
			            	if(anno.length()!=4) {
			            		throw new ResponseStatusException(HttpStatus.LOCKED);
			            	}
		            		archivo = new File(DIRECTORIO_UPLOAD+"/"+lines[j].split(" ")[1]);
		            		if (!archivo.exists()) {
		            		    archivo.mkdirs();
		            		}
		            		archivo = new File(DIRECTORIO_UPLOAD+"/"+anno+"/"+mes);
		            		if (!archivo.exists()) {
		            		    archivo.mkdirs();
		            		}
		            	}
		            	int variable=20;
		            	if(anno.equalsIgnoreCase("2020")) {
		            		variable=20;
		            	}else if(anno.equalsIgnoreCase("2021")) {
		            		variable=19;
		            	}
		            	if(j==variable) {
		            		
		            		PDDocument document = new PDDocument();
		            		PDPage page = pdDocument.getPage(i-1);
		            		document.addPage(page);
		            		String nomArchivo=mes+"_"+anno+"_"+lines[j]+".pdf";
		                    document.save(new File(DIRECTORIO_UPLOAD+"/"+anno+"/"+mes+"/"+nomArchivo) );
		            		document.close();
		            		boletas.add(new BoletaPagoDTO(lines[j], nomArchivo, anno, mes));
		            	}
					}
				}
	          

	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (pdDocument != null) {
	                try {
	                    pdDocument.close();
	                    
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return boletas;
	    }
	   
	   public Resource cargar(String anno,String mes,String nombreFoto) throws MalformedURLException {
			Path rutaArchivo = getPath(anno+"/"+mes+"/"+nombreFoto);

			Resource recurso = new UrlResource(rutaArchivo.toUri());
		
			
			return recurso;
		}
	   
	   
		public Path getPath(String nombreFoto) {
		 return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
		}

}
