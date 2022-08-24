package com.flaviojunior.financas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MensagemService {

	@Value("${application.name}")
	private String appName;
	
}
