package com.flaviojunior.financas.service;

import com.flaviojunior.financas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email,String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	public void validarEmail(String email);

}
