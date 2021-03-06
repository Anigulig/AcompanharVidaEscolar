package com.jeffersonantunes.ave.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.activity.CadastroUsuarioActivity;
import com.jeffersonantunes.ave.activity.ChamadaActivity;
import com.jeffersonantunes.ave.activity.LoginActivity;
import com.jeffersonantunes.ave.activity.MainActivity;
import com.jeffersonantunes.ave.helper.Preferencias;
import com.jeffersonantunes.ave.model.Nota;
import com.jeffersonantunes.ave.model.Professor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfessorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfessorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfessorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Nota nota;

    private TextView txtvwCadastrarProfessor;
    private EditText txtMatriculaProfessor;
    private EditText txtNomeProfessor;
    private EditText txtDisciplina;
    private EditText txtTurmaChamada;
    private EditText txtData;
    private EditText txtMatriculaProfessorLogado;
    private EditText txtNotaMatricula;
    private EditText txtNotaDisciplina;
    private EditText txtNota;
    private Button btnCadastrarProfessor;
    private Button btnChamada;
    private Button btnInserirNota;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Dia;

    private View view;

    private OnFragmentInteractionListener mListener;

    public ProfessorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfessorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfessorFragment newInstance(String param1, String param2) {
        ProfessorFragment fragment = new ProfessorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_professor, container, false);

        txtvwCadastrarProfessor                    = (TextView) view.findViewById(R.id.txtvwCadastrarProfessor);
        txtMatriculaProfessor                      = (EditText) view.findViewById(R.id.txtMatriculaProfessor);
        txtNomeProfessor                           = (EditText) view.findViewById(R.id.txtNomeProfessor);
        txtDisciplina                              = (EditText) view.findViewById(R.id.txtDisciplina);
        txtTurmaChamada                            = (EditText) view.findViewById(R.id.txtTurmaChamada);
        txtData                                    = (EditText) view.findViewById(R.id.txtDataChamada);
        txtMatriculaProfessorLogado                = (EditText) view.findViewById(R.id.txtMatriculaProfessorLogado);
        txtNotaMatricula                           = (EditText) view.findViewById(R.id.txtNotaMatricula);
        txtNotaDisciplina                          = (EditText) view.findViewById(R.id.txtNotaDisciplina);
        txtNota                                    = (EditText) view.findViewById(R.id.txtNota);
        btnCadastrarProfessor                      = (Button) view.findViewById(R.id.btnCadastrarProfessor);

// get date now

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Dia = simpleDateFormat.format(calendar.getTime());
        txtData.setText(Dia);


        //verificando acesso do usuario
        Preferencias preferencias = new Preferencias(getActivity());

        String acesso = preferencias.getAcessoUsuario();

        if ( acesso.equals("administrador")){
            txtvwCadastrarProfessor.setText("Cadastrar Professor");
            txtDisciplina.setEnabled(true);
            txtNomeProfessor.setEnabled(true);
            txtMatriculaProfessor.setEnabled(true);
            btnCadastrarProfessor.setEnabled(true);
        }

        btnCadastrarProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaEditText("professor")){

                    Professor professor = new Professor();

                    professor.setDisciplina(txtDisciplina.getText().toString());
                    professor.setNome(txtNomeProfessor.getText().toString());
                    professor.setMatricula(Integer.parseInt(txtMatriculaProfessor.getText().toString()));

                    professor.salvar();

                    Toast.makeText(getActivity(), "Novo professor adicionado.", Toast.LENGTH_LONG).show();

                    txtDisciplina.setText("");
                    txtNomeProfessor.setText("");
                    txtMatriculaProfessor.setText("");

                }
            }
        });


        btnChamada                     = (Button) view.findViewById(R.id.btnFazerChamada);
        btnChamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaEditText("chamada")){
                    abrirChamada();
                }
            }
        });

        btnInserirNota                  = (Button) view.findViewById(R.id.btnInserirNota);
        btnInserirNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validaEditText("nota")) {

                    salvarNotaAluno();

                    txtNotaMatricula.setText("");
                    txtNotaDisciplina.setText("");
                    txtNota.setText("");
                }
            }
        });

        return view;
    }

    private void salvarNotaAluno(){

        nota = new Nota();

        nota.setMatricula(Integer.parseInt(txtNotaMatricula.getText().toString()));
        nota.setMatriculaProfessor(Integer.parseInt(txtMatriculaProfessorLogado.getText().toString()));
        nota.setNota(Integer.parseInt(txtNota.getText().toString()));
        nota.setDisciplina(txtNotaDisciplina.getText().toString());

        nota.salvar();

        Toast.makeText(getActivity(), "Nota do aluno lançada.", Toast.LENGTH_LONG).show();

    }

    private void abrirChamada(){
        Intent intent = new Intent(getActivity(), ChamadaActivity.class);
        intent.putExtra("data",txtData.getText().toString());
        intent.putExtra("turma",txtTurmaChamada.getText().toString());
        intent.putExtra("matriculaProfessor", txtMatriculaProfessorLogado.getText().toString());
        startActivity(intent);
    }
    private boolean validaEditText(String bloco){

        switch (bloco) {
            case "professor":
                if (txtMatriculaProfessor.getText().length() <= 0
                        || txtNomeProfessor.getText().length() <= 0
                        || txtDisciplina.getText().length() <= 0) {

                    Toast.makeText(getActivity(), "Por favor preencha todos os campos para continuar.", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    return true;
                }
            case "chamada":
                if (txtTurmaChamada.getText().length() <= 0
                        || txtData.getText().length() <= 0
                        || txtMatriculaProfessorLogado.getText().length() <=0) {

                    Toast.makeText(getActivity(), "Por favor preencha todos os campos para continuar.", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    return true;
                }
            case "nota":
                if (txtNotaMatricula.getText().length() <= 0
                        || txtNotaDisciplina.getText().length() <= 0
                        || txtNota.getText().length() <= 0
                        || txtMatriculaProfessorLogado.getText().length() <=0) {

                    Toast.makeText(getActivity(), "Por favor preencha todos os campos para continuar.", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    return true;
                }
            default:
                return false;
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
