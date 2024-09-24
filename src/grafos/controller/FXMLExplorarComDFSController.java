/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos.controller;

import grafos.algoritmos.AlgoritmoDFS;
import grafos.arquivo.In;
import grafos.model.DFS;
import grafos.model.Desenho;
import grafos.model.Grafo;
import grafos.runnable.RunnableDFS;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Jonathas
 */
public class FXMLExplorarComDFSController implements Initializable {

    @FXML
    private AnchorPane anchorPaneGrafo; //anchorPane onde se deve desenhar o grafo
    @FXML
    private AnchorPane anchorPane;      //anchorPane da tela

    //botões
    @FXML
    private Button continuar;
    @FXML
    private Button parar;
    @FXML
    private Button play;
    @FXML
    private Button acelerar;
    @FXML
    private Button desacelerar;
    @FXML
    private Button voltar;
    @FXML
    private Button VerticeExplorando;
    @FXML
    private Button variaveis;

    //Tabela de parâmetros
    @FXML
    public TableView<DFS> tableViewPropriedades;
    @FXML
    private TableColumn<DFS, Integer> v;
    @FXML
    private TableColumn<DFS, String> marcado;
    @FXML
    private TableColumn<DFS, String> arestaPara;

    //List para código, lista de adjacências e representação da pilha de execução
    @FXML
    private ListView listViewCodigo;
    @FXML
    private ListView listaViewADJ;
    @FXML
    private ListView listViewpilhaDeExecucao;

    @FXML
    private GridPane codigoGrid;   //recebe o código DFS

    //List de string utilizadas para preencher a listViewCodigo, listaViewADJ, listaViewADJ
    private final List<String> listAdjacencia = new ArrayList<>();
    private final List<String> listPilha = new ArrayList<>();
    private final List<String> listCodigo = new ArrayList<>();

    //private ObservableList<String> utilizados no listAdjacencia, listPilha, listCodigo;
    private ObservableList<String> observableListCodigo;
    private ObservableList<String> observableListPilha;
    private ObservableList<String> observableListADJ;
    private ObservableList<DFS> observableListBFS;

    //elementos utilizados para manipular o gridPane com o codigo
    private Pane[] codigoPane;
    private Label[] codigolabel;
    public static int pausado;
    public Thread threadExecutarDFS;

    //Componentes utilizados para armazear os desenhos das linhas, circulos e texto (ID) do grafo
    Group componentesLine = new Group();
    Group componentesCircle = new Group();
    Group componentesText = new Group();

    public Grafo grafo;             //elemento do tipo grafo com as arestas e grafos para ser explorado
    public int velocidade;          //velocidade de execução do algoritmo. Por hora o set é manual
    public RunnableDFS runnableDFS; //objeto do tipo Runnable utilizado para alterar os elementos da interface grafica. Ele é instanciado no método executarBFS
    public Thread threadDFS;        //Thread que utiliza o runnableBFS
    public DFS item;                //elemento utilizado para insersão na tabela

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initListeners();

        FXMLExplorarComDFSController.pausado = 0;
        listViewpilhaDeExecucao.setStyle("-fx-font-weight: 900; -fx-font-size: 17px; -fx-border-width: 1; -fx-border-color: #000000;");

        //desenhando o grafo
        desenharGrafo();

        //inicializar a atbela de parametros do algoritmo. A atualização será feita no decorrer da execução do código
        inicializarTabelaParametros();
        //preencherTabelaParametros();

        //Adicionado o código do DFS a exibição
        adicionarCodigo();

        //adicionando a lista de adjacencias para exibição
        carregarListaDeAdjacencias();

        try {
            //iniciando o DFS
            executarDFS();
        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLExplorarComDFSController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * inicia os listners do botão
     */
    private void initListeners() {
        VerticeExplorando.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ScaleTransition transition = new ScaleTransition(
                        Duration.millis(500), VerticeExplorando);
                transition.setToX(1.2);
                transition.setToY(1.2);
                transition.play();
            }
        });
        VerticeExplorando.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ScaleTransition transition = new ScaleTransition(
                        Duration.millis(500), VerticeExplorando);
                transition.setToX(1.0);
                transition.setToY(1.0);
                transition.play();
            }
        });
    }

    /**
     * Ação do botão voltar
     *
     * @throws IOException excepion
     */
    @FXML
    public void handleButtonVoltar() throws IOException {

        runnableDFS.setExecutar(false);
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/grafos/view/FXMLVBoxMain.fxml"));
        anchorPane.getChildren().setAll(a);

    }

    /**
     * Ação do botão pause
     * @throws IOException excepion
     * @throws InterruptedException  excepion
     */
    @FXML
    public void handleButtonPause() throws IOException, InterruptedException {

        FXMLExplorarComDFSController.pausado = 1;

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

        FXMLExplorarComDFSController.pausado = 0;
    }

    /**
     * Ação do botão acelerar. utilizado para aumentar a velocidade de execução do algoritmo
     * @throws IOException excepion
     * @throws InterruptedException  excepion
     */
    @FXML
    public void handleButtonAcelerar() throws IOException, InterruptedException {
        if (AlgoritmoDFS.velocidade >= 200) {
            AlgoritmoDFS.velocidade -= 100;
        }
        
    }

    /**
     * Ação do botão desacelerar. utilizado para diminuir a velocidade de execução do algoritmo
     * @throws IOException excepion
     * @throws InterruptedException  excepion
     */
    @FXML
    public void handleButtonDesacelerar() throws IOException, InterruptedException {

        AlgoritmoDFS.velocidade += 100;
        

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
        
        grafo = new Grafo(in);

        criarGrafo.desenharGrafo(grafo, anchorPaneGrafo, componentesLine, componentesCircle, componentesText);

    }

    /**
     * Popula a tabela de parâmetros
     */
    public void inicializarTabelaParametros() {

        v.setCellValueFactory(new PropertyValueFactory<>("id"));
        marcado.setCellValueFactory(new PropertyValueFactory<>("itemMarcado"));
        arestaPara.setCellValueFactory(new PropertyValueFactory<>("arestaPara"));

    }


    /**
     * Adiciona o código do algoritmo que o usuário esqueceu em um grid
     */
    private void adicionarCodigo() {
        codigolabel = new Label[23];
        codigoPane = new Pane[23];

        for (int i = 0; i < 23; i++) {
            codigoPane[i] = new Pane();
            codigolabel[i] = new Label();

            codigoGrid.add(codigoPane[i], 1, i);
            codigolabel[i].setStyle("-fx-font-size: 14px;");
            codigoPane[i].setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");
        }

        codigolabel[0].setText("public class AlgoritmoDFSGrafo {");
        codigolabel[1].setText("    private booblean[] marcado;");
        codigolabel[2].setText("    private int[] arestaPara;");
        codigolabel[3].setText("");
        codigolabel[4].setText("    public AlgoritmoDFSGrafo(Grafo G, int vo){");
        codigolabel[5].setText("            this.vo = vo;");
        codigolabel[6].setText("            arestaPara = new int[G.V()];");
        codigolabel[7].setText("            marcado = new boolean[G.V()];");
        codigolabel[8].setText("            dfs(G, vo);");
        codigolabel[9].setText("    }");
        codigolabel[10].setText("");

        codigolabel[11].setText("    private void dfs(Grafo G, int v) {");
        codigolabel[12].setText("            marcado[v] = true;");
        codigolabel[13].setText("            for (Aresta a : G.adj(v)) {");
        codigolabel[14].setText("                    int x = a.getV2();");
        codigolabel[15].setText("                    if (!marcado[x]) {");
        codigolabel[16].setText("                            arestaPara[x] = v;");
        codigolabel[17].setText("                            dfs(G, x);");
        codigolabel[18].setText("                    }");
        codigolabel[19].setText("            }");
        codigolabel[20].setText("    }");
        codigolabel[21].setText("}");

        for (int i = 0; i < 23; i++) {
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
        listaViewADJ.setStyle("-fx-font-weight: 900; -fx-font-size: 16px; -fx-text-align: center;  -fx-border-width: 1; -fx-border-color: #000000;");
    }

    
    /**
     * Este método instância e inicia a runnable e a thread responsáveis pela execução do algoritmo e pela manutenção da interface gráfica.
     * @throws InterruptedException excepion
     */
    private void executarDFS() throws InterruptedException {

        
        runnableDFS = new RunnableDFS(listViewpilhaDeExecucao, tableViewPropriedades, grafo, FXMLVBoxMainController.origem, codigoPane, componentesLine, componentesCircle, VerticeExplorando, Thread.currentThread(), variaveis);
        threadDFS = new Thread(runnableDFS);
        threadDFS.setName("Thread DFS");
        threadDFS.setDaemon(true);
        threadDFS.start();

    }

}
