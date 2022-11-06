package com.andpedroso.firebaselogincadastro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.andpedroso.firebaselogincadastro.config.ConfiguracaoFirebase;
import com.andpedroso.firebaselogincadastro.helper.Base64Custom;
import com.andpedroso.firebaselogincadastro.helper.UsuarioFirebase;
import com.andpedroso.firebaselogincadastro.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private TextInputEditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        campoNome = findViewById(R.id.nome_cadastro_input);
        campoEmail = findViewById(R.id.email_cadastro_input);
        campoSenha = findViewById(R.id.senha_cadastro_input);
    }
    public void cadastrarUsuario(@NonNull Usuario usuario){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(
                            CadastroActivity.this,
                            "Sucesso ao cadastrar usuário",
                            Toast.LENGTH_SHORT
                    ).show();
                    UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                    finish();
                    try {
                        String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                        usuario.setId(identificadorUsuario);
                        usuario.salvar();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um E-mail válido";
                    } catch (FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já foi cadastrada";
                    } catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " +
                                e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(
                            CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
    public void validarCadastroUsuario(View view){
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        if (!textoNome.isEmpty()) {
            if (!textoEmail.isEmpty()){
                if (!textoSenha.isEmpty()){
                    Usuario usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    cadastrarUsuario(usuario);
                } else {
                    Toast.makeText(
                            CadastroActivity.this,
                            "Preencha a senha",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } else {
                Toast.makeText(
                        CadastroActivity.this,
                        "Preencha o email",
                        Toast.LENGTH_SHORT
                ).show();
            }
        } else {
            Toast.makeText(
                    CadastroActivity.this,
                    "Preencha o nome",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}