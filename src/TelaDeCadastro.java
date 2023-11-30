import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class TelaDeCadastro extends JDialog {
    private JTextField tf_cor;
    private JTextField tf_name;
    private JTextField tf_tamanho;
    private JTextField tf_sexo;
    private JTextField tf_raca;
    private JTextField tf_idade;
    private JTextField tf_historia;
    private JLabel showImagem;
    private JButton btUploadImage;
    private JButton btCancelar;
    private JButton btSalvar;
    private JPanel tela;

    public TelaDeCadastro(JFrame parent) {
        super(parent);

        setTitle("Cadastro de Pets");
        setContentPane(tela);
        setMinimumSize(new Dimension(650, 674));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastroPet();
            }
        });
        btCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

//        setVisible(true);
        btUploadImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfile = new JFileChooser();
                jfile.setCurrentDirectory(new File(System.getProperty("user.home")));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Image","jpg","png","jpeg");
                jfile.addChoosableFileFilter(filter);

                int result = jfile.showSaveDialog(null);
                System.out.println(""+result);

                File arquivoSelecionado = jfile.getSelectedFile();
                String filename = arquivoSelecionado.getName();
                if(filename.endsWith(".jpg")||filename.endsWith(".JPG")||filename.endsWith(".png")||filename.endsWith(".PNG")||filename.endsWith(".jpeg")||filename.endsWith(".JPEG")){
                    String path = arquivoSelecionado.getAbsolutePath();
                    ImageIcon imagem = new ImageIcon(path);

                    Image img = imagem.getImage();
                    Image novaImagem = img.getScaledInstance(showImagem.getWidth(),showImagem.getHeight(),Image.SCALE_SMOOTH);

                    ImageIcon mostrarImagem = new ImageIcon(novaImagem);
                    showImagem.setIcon(mostrarImagem);
                }
                String path = arquivoSelecionado.getAbsolutePath();
                System.out.println(""+filename);
                System.out.println(""+ path);

            }
        });

        setVisible(true);
    }


    private void cadastroPet() {
        String nome = tf_name.getText();
        String raca = tf_raca.getText();
        String tamanho = tf_tamanho.getText();
        String sexo = tf_sexo.getText();
        String cor = tf_cor.getText();
        String idade = tf_idade.getText();
        String historia = tf_historia.getText();
//        byte [] imagem =;
        if (nome.isEmpty() || raca.isEmpty() || tamanho.isEmpty() || sexo.isEmpty() || cor.isEmpty() || historia.isEmpty() ){
            JOptionPane.showConfirmDialog(this, "Preencha todas as informações", "Tente novamente", JOptionPane.ERROR_MESSAGE);
            return;
        }
        pet = inserirPetNoBanco (nome, raca, tamanho, sexo, cor, idade, historia);
        if(pet != null){
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Não consegui registra o Pet no banco de dados", "Tente novamente", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Pet pet;
    private Pet inserirPetNoBanco(String nome, String raca, String tamanho, String sexo, String cor, String idade, String historia) {
        Pet pet = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/pets";
        final String USERNAME = "root";
        final String PASSWORD = "Ramises229@";
        try{
            Connection con = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            try {
                fis = new FileInputStream(path);
//                Blob imagemBlob = con.createBlob();
//                imagemBlob.setBytes(1, pets.getImagem());
                //Aqui já é a inserçao)
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO pets (nome, raca, tamanho, sexo, cor, idade, historia) VALUES (?, ?, ?, ?, ?, ?, ?) ");
                pstmt.setString(1, nome);
                pstmt.setString(2, raca);
                pstmt.setString(3, tamanho);
                pstmt.setString(4, sexo);
                pstmt.setString(5, cor);
                pstmt.setString(6, idade);
                pstmt.setString(7, historia);
//                pstmt.setBlob(8, imagemBlob);

                int addedRows = pstmt.executeUpdate();
                if (addedRows >0){
                    pet = new Pet();
                    pet.nome = nome;
                    pet.raca = raca;
                    pet.tamanho = tamanho;
                    pet.sexo = sexo;
                    pet.cor = cor;
                    pet.idade = idade;
                    pet.historia = historia;
                }
            } finally {
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pet;
    }


    public static void main(String[] args) {
        TelaDeCadastro telaDeCadastro = new TelaDeCadastro(null);
        Pet pet = telaDeCadastro.pet;
        if (pet != null){
            System.out.println("Sucesso em registrar o Pet");
        } else {
            System.out.println("Registro cancelado");
        }
    }

}
