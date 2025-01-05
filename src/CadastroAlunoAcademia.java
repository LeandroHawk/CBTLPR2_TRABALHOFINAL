import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Leandro Felix e João Marcos

public class CadastroAlunoAcademia {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cadastro de Aluno de Academia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 5, 5));

        // Campos de entrada
        JLabel lblNome = new JLabel("Nome:");
        JTextField txtNome = new JTextField();
        JLabel lblIdade = new JLabel("Idade:");
        JTextField txtIdade = new JTextField();
        JLabel lblPeso = new JLabel("Peso (kg):");
        JTextField txtPeso = new JTextField();
        JLabel lblAltura = new JLabel("Altura (m):");
        JTextField txtAltura = new JTextField();
        JLabel lblObjetivo = new JLabel("Objetivo:");
        JTextField txtObjetivo = new JTextField();

        // Adicionando componentes ao painel
        panel.add(lblNome);
        panel.add(txtNome);
        panel.add(lblIdade);
        panel.add(txtIdade);
        panel.add(lblPeso);
        panel.add(txtPeso);
        panel.add(lblAltura);
        panel.add(txtAltura);
        panel.add(lblObjetivo);
        panel.add(txtObjetivo);

        // Botões
        JButton btnIncluir = new JButton("Incluir");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnApresentar = new JButton("Apresenta Dados");
        JButton btnSair = new JButton("Sair");

        panel.add(btnIncluir);
        panel.add(btnLimpar);
        panel.add(btnApresentar);
        panel.add(btnSair);

        frame.add(panel);
        frame.setVisible(true);

        // Ação do botão Incluir
        btnIncluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                int idade = Integer.parseInt(txtIdade.getText());
                float peso = Float.parseFloat(txtPeso.getText());
                float altura = Float.parseFloat(txtAltura.getText());
                String objetivo = txtObjetivo.getText();

                try {
                    Connection connection = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/academia", 
                            "root", 
                            "*Consagrado712"
                    );

                    String query = "INSERT INTO alunos (nome, idade, peso, altura, objetivo) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, nome);
                    preparedStatement.setInt(2, idade);
                    preparedStatement.setFloat(3, peso);
                    preparedStatement.setFloat(4, altura);
                    preparedStatement.setString(5, objetivo);

                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(frame, "Dados incluídos com sucesso!");
                    }

                    connection.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao conectar ao banco de dados: " + ex.getMessage());
                }
            }
        });

        // Ação do botão Apresentar Dados (com conversão para JSON)
        btnApresentar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/academia",
                            "root",
                            "*Consagrado712"
                    );
        
                    String query = "SELECT * FROM alunos";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();
        
                    StringBuilder result = new StringBuilder();
        
                    while (resultSet.next()) {
                        String nome = resultSet.getString("nome");
                        int idade = resultSet.getInt("idade");
                        float peso = resultSet.getFloat("peso");
                        float altura = resultSet.getFloat("altura");
                        String objetivo = resultSet.getString("objetivo");
        
                        // Construindo um objeto JSON para cada aluno
                        String alunoJson = String.format(
                                "{\n  \"nome\": \"%s\",\n  \"idade\": %d,\n  \"peso\": %.2f,\n  \"altura\": %.2f,\n  \"objetivo\": \"%s\"\n}",
                                nome, idade, peso, altura, objetivo
                        );
        
                        // Adicionando o objeto JSON ao resultado
                        result.append(alunoJson).append(",\n");
                    }
        
                    // Remover a última vírgula e newline
                    if (result.length() > 0) {
                        result.setLength(result.length() - 2);  // Remove a última vírgula
                    }
        
                    // Exibir os dados no formato JSON dentro de um JTextArea
                    JTextArea textArea = new JTextArea(result.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 300));
        
                    JOptionPane.showMessageDialog(frame, scrollPane, "Dados dos Alunos", JOptionPane.INFORMATION_MESSAGE);
                    connection.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao conectar ao banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ação do botão Limpar
        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtNome.setText("");
                txtIdade.setText("");
                txtPeso.setText("");
                txtAltura.setText("");
                txtObjetivo.setText("");
            }
        });

        // Ação do botão Sair
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}