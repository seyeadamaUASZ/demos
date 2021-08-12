package com.gl.sid;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class FichierController {
	
	@Autowired
	private FichierRepository repos;
	
	 @RequestMapping(value = "fichier/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Fichier createFichier(HttpServletRequest request, MultipartFile file) {
		 Map<String, ?> map=request.getParameterMap();
		 Fichier f=new Fichier();
		 try {
			 f.setCni(file.getBytes());
			 
		 }catch(IOException e) {
			e.printStackTrace(); 
		 }
		 for(Map.Entry<String, ?>entry:map.entrySet()) {
			 Field field=null;
			 try {
				 field=Fichier.class.getDeclaredField(entry.getKey().trim());
				 field.setAccessible(true);
			 }catch(NoSuchFieldException e) {
				 e.printStackTrace();
			 }catch(SecurityException e) {
				 e.printStackTrace();
			 }
			 try {
				 ArrayList<String> longs=new ArrayList<String>(Arrays.asList("id"));
				 if(longs.contains(entry.getKey())) {
					 field.set(f, Long.parseLong((String) (((String[]) entry.getValue())[0].equals("null") ? "0" : ((String[]) entry.getValue())[0])));
				 }else if (entry != null && entry.getKey().contains("file")) {
	                    if (entry instanceof MultipartFile) {
	                        field.set(f, ((MultipartFile) ((Object[]) entry.getValue())[0]).getBytes());

	                    }

	                }else {
	                    field.set(f, ((String[]) entry.getValue())[0]);
	                }
				 
			 }catch(IllegalArgumentException e) {
				 e.printStackTrace();
			 }catch(IllegalAccessException e) {
				 e.printStackTrace();
			 }catch(IOException e) {
				 e.printStackTrace();
			 }
		 }
		 this.repos.save(f);
		 return f;
	 }
}
