package com.andpedroso.firebaselogincadastro.model;

import com.andpedroso.firebaselogincadastro.config.ConfiguracaoFirebase;
import com.andpedroso.firebaselogincadastro.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String foto;
    public Usuario() {
    }
    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("usuarios").child(getId());
        usuario.setValue(this);
    }
    public void atualizar(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarioRef =
                database.child("usuarios")
                        .child(identificadorUsuario);
        Map<String, Object> valoresUsuario = converterParaMap();
        usuarioRef.updateChildren(valoresUsuario);
    }
    @Exclude
    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("foto", getFoto());
        return usuarioMap;
    }
    public String getFoto() {return foto;}
    public void setFoto(String foto) {this.foto = foto;}
    @Exclude
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
