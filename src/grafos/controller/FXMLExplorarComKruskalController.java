/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos.controller;


import grafos.algoritmos.AlgoritmoMSTKruskalGrafoPonderado;
import grafos.arquivo.In;
import grafos.model.Desenho;
import grafos.model.GrafoPonderado;
import grafos.model.KruskalItemTablewViewMST;
import grafos.model.KruskalItemTablewViewPQ;
import grafos.runnable.RunnableKruskal;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Jonathas
 */
public class FXMLExplorarComKruskalController implements Initializable {

    @FXML
    private AnchorPane anchorPaneGrafo; //AnchorPane onde se deve desenhra o Grafo
    @FXML
    private AnchorPane anchorPane;      //AnchorPane da tela 
    @FXML
    
    // Botões
    private Button parar;  
    @FXML
    private Button play;
    @FXML
    private Button acelerar;
    @FXML
    private Button desacelerar;
    @FXML
    private Button voltar;
    
    // Botões
    @FXML
    private Button variaveis;           //botão utilizado para exibir o valor das váriaveis chave do código

    //Tabela com as arestas de MST
    @FXML
    public TableView<KruskalItemTablewViewMST> tableViewMST;
    @FXML
    private TableColumn<KruskalItemTablewViewMST, String> tableColumnMSTAresta;
    
    //Tabela com as arestas de PQ
    @FXML
    public TableView<KruskalItemTablewViewPQ> tableViewPQ;
    @FXML
    private TableColumn<KruskalItemTablewViewPQ, String> tableColumnPQAresta;
    @FXML
    private TableColumn<KruskalItemTablewViewPQ, Double> tableColumnPQPeso;
    
    @FXML
    private ListView listaViewADJ;   //list com a lista de adjacencias do grafo
    
    
    @FXML
    private GridPane codigoGrid;     //recebe o código BFS

    //Listas de String utilizadas para preencher a listViewCodigo, listaViewADJ, filaView
    private final List<String> listAdjacencia = new ArrayList<>();
    private final List<String> listCodigo = new ArrayList<>();
    
    private ObservableList<String> observableListADJ;

    //elementos utilizados para manipular o gridPane com o codigo
    private Pane[] codigoPane;
    private Label[] codigolabel;

    //Componentes utilizados para armazear os desenhos das linhas, circulos e texto (ID) do grafo
    Group componentesLine = new Group();
    Group componentesCircle = new Group();
    Group componentesText = new Group();

    public GrafoPonderado grafo;             //elemento do tipo grafo com as arestas e grafos para ser explorado
    public RunnableKruskal runnableKruskal;  //objeto do tipo Runnable utilizado para alterar os elementos da interface grafica. Ele é instanciado no método executarKruskal 
    public Thread threadKruskal;             //Thread que utiliza o runnableKruskal
    public static int pausado;               //variável que marca quando a execução esta parada ou não

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        //desenhando o grafo
        desenharGrafo();

        //inicializar a atbela de parametros do algoritmo. A atualização será feita no decorrer da execução do código
        inicializarTabelas();
        

        //Adicionado o código do Kruskal a exibição
        adicionarCodigo();

        //adicionando a lista de adjacencias para exibição
        carregarListaDeAdjacencias();

        try {
            //iniciando o Kruskal
            executarKruskal();
        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLExplorarComDFSController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Ação do botão voltar
     * @throws IOException excepion
     */
    @FXML
    public void handleButtonVoltar() throws IOException {
        runnableKruskal.setExecutar(false);

        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/grafos/view/FXMLVBoxMain.fxml"));
        anchorPane.getChildren().setAll(a);
   
    }

    /**
     * Ação do botão pause
     * @throws IOException excepion
     * @throws InterruptedException excepion
     */
    @FXML
    public void handleButtonPause() throws IOException, InterruptedException {

        FXMLExplorarComKruskalController.pausado = 1;

    }

    /**
     * Ação do botão play (Cancela o pause)
     * @throws IOException excepion
     * @throws InterruptedException  excepion
     */
    @FXML
    public void handleButtonPlay() throws IOException, InterruptedException {

        synchronized (Thread.currentThread()) {
            Thread.currentThread().notify();
        }

        FXMLExplorarComKruskalController.pausado = 0;
    }

    /**
     * Ação do botão acelerar. utilizado para aumentar a velocidade de execução do algoritmo
     * @throws IOException excepion
     * @throws InterruptedException  excepion
     */
    @FXML
    public void handleButtonAcelerar() throws IOException, InterruptedException {
        if (AlgoritmoMSTKruskalGrafoPonderado.velocidade >= 200) {
            AlgoritmoMSTKruskalGrafoPonderado.velocidade -= 100;
        }

    }

    /**
     * Ação do botão desacelerar. utilizado para diminuir a velocidade de execução do algoritmo
     * @throws IOException excepion
     * @throws InterruptedException excepion
     */
    @FXML
    public void handleButtonDesacelerar() throws IOException, InterruptedException {

        AlgoritmoMSTKruskalGrafoPonderado.velocidade += 100;

    }

    /**
     * 
     * @param text texto(Id) a ser inserido no circulo
     * @param circle circulo que receberá o texto
     */
    private void centralizarText(Text text, Circle circle) {
        double W = text.getBoundsInLocal().getWidth();
        double H = text.getBoundsInLocal().getHeight();
        text.relocate(circle.getCenterX() - W / 2, circle.getCenterY() - H / 2);
    }

    /**
     * Carrega o grafo a partir de um arquivo txt, escolhido na tela anterior e o desenha
     */
    private void desenharGrafo() {

        Desenho criarGrafo = new Desenho();

        In in = new In(FXMLVBoxMainController.arquivoGrafo);
        
        // para ler a primeira linha do arquivo
        String temp = new String(in.readString());
        temp = null;
        
        grafo = new GrafoPonderado(in);

        criarGrafo.desenharGrafo(grafo, anchorPaneGrafo, componentesLine, componentesCircle, componentesText);

    }

    /**
     * Popula as tabelas (TableView's)
     */
    public void inicializarTabelas() {
        tableColumnMSTAresta.setCellValueFactory(new PropertyValueFactory<>("aresta"));
        tableColumnPQAresta.setCellValueFactory(new PropertyValueFactory<>("aresta"));
        tableColumnPQPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
    }

    /**
     * Adiciona o código do algoritmo que o usuário esqueceu em um grid
     */
    private void adicionarCodigo() {
        codigolabel = new Label[22];
        codigoPane = new Pane[22];

        for (int i = 0; i < 22; i++) {
            codigoPane[i] = new Pane();
            codigolabel[i] = new Label();

            codigoGrid.add(codigoPane[i], 1, i);
            codigolabel[i].setStyle("-fx-font-size: 14px;");
            codigoPane[i].setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");

        }

        codigolabel[0].setText("  public AlgoritmoMSTKruskal {");
        codigolabel[1].setText("    private double peso;");
        codigolabel[2].setText("    private Fila<Aresta> mst = new Fila<Aresta>();");
        codigolabel[3].setText("");
        codigolabel[4].setText("    public AlgoritmoMSTKruskal(Grafo G) {");
        codigolabel[5].setText("      FilaMin<Aresta> pq = new FilaMin<Aresta>();");
        codigolabel[6].setText("      for (Aresta e : G.arestas()) {");
        codigolabel[7].setText("          pq.insere(e);");
        codigolabel[8].setText("      }");
        codigolabel[9].setText("      UF uf = new UF(G.V());");
        codigolabel[10].setText("      while (!pq.isEmpty()) {");
        codigolabel[11].setText("        Aresta a = pq.delMin();");
        codigolabel[12].setText("        int v1 = a.umVertice().getId();");
        codigolabel[13].setText("        int v2 = a.outroVertice(a.umVertice()).getId();");
        codigolabel[14].setText("        if (!uf.conectado(v1, v2)) {");
        codigolabel[15].setText("            uf.junta(v1, v2);");
        codigolabel[16].setText("            mst.enfileira(a);");
        codigolabel[17].setText("            peso += a.getPeso();");
        codigolabel[18].setText("        }");
        codigolabel[19].setText("     }");
        codigolabel[20].setText("   }");
        codigolabel[21].setText("}");

        for (int i = 0; i < 22; i++) {
            codigoGrid.add(codigolabel[i], 1, i);
        }
    }

    /**
     * Carrega a lista de adjac~encias do grafo escolhido na tela anterior
     */
    private void carregarListaDeAdjacencias() {

        String adj = "";
        for (int i = 0; i < grafo.V(); i++) {
            adj += i;
            adj += "  --  ";
            for (int j = 0; j < grafo.getAdj()[i].size(); j++) {
                adj += grafo.getAdj()[i].get(j).getV2().getId();
                if (j < grafo.getAdj()[i].size() - 1) {
                    adj += " - ";
                }
            }
            listAdjacencia.add(adj);
            adj = "";
        }

        observableListADJ = FXCollections.observableArrayList(listAdjacencia);
        listaViewADJ.setItems(observableListADJ);
        listaViewADJ.setStyle("-fx-font-weight: 900; -fx-font-size: 18px;");
    }

    /**
     * Este método instância e inicia a runnable e a thread responsáveis pela execução do algoritmo e pela manutenção da interface gráfica.
     * @throws InterruptedException  excepion
     */
    private void executarKruskal() throws InterruptedException {
        runnableKruskal = new RunnableKruskal(grafo, Thread.currentThread(), componentesLine, componentesCircle, codigoPane, tableViewMST, tableViewPQ, variaveis);
        threadKruskal = new Thread(runnableKruskal);
        threadKruskal.setName("Thread Kruskal");
        threadKruskal.setDaemon(true);
        threadKruskal.start();
    }

}
