/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos.controller;


import grafos.algoritmos.AlgoritmoBFS;
import grafos.arquivo.In;
import grafos.model.BFS;
import grafos.model.Desenho;
import grafos.model.Grafo;
import grafos.runnable.RunnableBFS;
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
public class FXMLExplorarComBFSController implements Initializable {

    @FXML
    private AnchorPane anchorPaneGrafo; //AnchorPane onde se deve desenhra o Grafo
    @FXML
    private AnchorPane anchorPane;      // AnchorPane da tela 
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
    private Button VerticeExplorando;   //botão utilizado para exibir qualvértice esta sendo explorado
    @FXML
    private Button variaveis;           //botão utilizado para exibir o valor das váriaveis chave do código

    //Tabela com os valores dos vetores do algoritmo
    @FXML
    public TableView<BFS> tableViewPropriedades;
    @FXML
    private TableColumn<BFS, Integer> v;
    @FXML
    private TableColumn<BFS, String> marcado;
    @FXML
    private TableColumn<BFS, String> arestaPara;
    @FXML
    private TableColumn<BFS, String> distanciaPara;

    
    
    @FXML
    private ListView listaViewADJ;   //list com a lista de adjacencias do grafo
    @FXML
    private ListView filaView;       //list para representar a fila de execução do algoritmo DFS
    
    
    @FXML
    private GridPane codigoGrid;     //recebe o código BFS

    //Listas de String utilizadas para preencher a listViewCodigo, listaViewADJ, filaView
    private final List<String> listAdjacencia = new ArrayList<>();
    private final List<String> listFila = new ArrayList<>();
    private final List<String> listCodigo = new ArrayList<>();
    
    
    //private ObservableList<String> utilizados no listAdjacencia, listFila, listCodigo;
    private ObservableList<String> observableListADJ;
    private ObservableList<String> observableListFila;
    private ObservableList<BFS> observableListBFS;

    
    //elementos utilizados para manipular o gridPane com o codigo
    private Pane[] codigoPane;
    private Label[] codigolabel;

    
    //Componentes utilizados para armazear os desenhos das linhas, circulos e texto (ID) do grafo
    Group componentesLine = new Group();
    Group componentesCircle = new Group();
    Group componentesText = new Group();

    
    public Grafo grafo;             //elemento do tipo grafo com as arestas e grafos para ser explorado
    public RunnableBFS runnableBFS; //objeto do tipo Runnable utilizado para alterar os elementos da interface grafica. Ele é instanciado no método executarBFS 
    public Thread threadBFS;        //Thread que utiliza o runnableBFS
    public static int pausado;      //variável que marca quando a execução esta parada ou não

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //inicia os listners do botão
        initListeners();
        
        //desenhando o grafo
        desenharGrafo();

        //inicializar a atbela de parametros do algoritmo. A atualização será feita no decorrer da execução do código
        inicializarTabelaParametros();
        

        //Adicionado o código do DFS a exibição
        adicionarCodigo();

        //adicionando a lista de adjacencias para exibição
        carregarListaDeAdjacencias();

        
        observableListFila = FXCollections.observableArrayList(listFila);
        filaView.setItems(observableListFila);
        filaView.setStyle("-fx-font-weight: 900; -fx-font-size: 18px;");

        try {
            //iniciando o DFS
            executarBFS();
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
     * @throws IOException excepion
     */
    @FXML
    public void handleButtonVoltar() throws IOException {
        runnableBFS.setExecutar(false);

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

        FXMLExplorarComBFSController.pausado = 1;

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

        FXMLExplorarComBFSController.pausado = 0;
    }

    /**
     * Ação do botão acelerar. utilizado para aumentar a velocidade de execução do algoritmo
     * @throws IOException excepion
     * @throws InterruptedException  excepion
     */
    @FXML
    public void handleButtonAcelerar() throws IOException, InterruptedException {
        if (AlgoritmoBFS.velocidade >= 200) {
            AlgoritmoBFS.velocidade -= 100;
        }

    }

    /**
     * Ação do botão desacelerar. utilizado para diminuir a velocidade de execução do algoritmo
     * @throws IOException excepion
     * @throws InterruptedException excepion
     */
    @FXML
    public void handleButtonDesacelerar() throws IOException, InterruptedException {

        AlgoritmoBFS.velocidade += 100;

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
        distanciaPara.setCellValueFactory(new PropertyValueFactory<>("distanciaPara"));

    }

    /**
     * Adiciona o código do algoritmo que o usuário esqueceu em um grid
     */
    private void adicionarCodigo() {
        codigolabel = new Label[33];
        codigoPane = new Pane[33];

        for (int i = 0; i < 33; i++) {
            codigoPane[i] = new Pane();
            codigolabel[i] = new Label();

            codigoGrid.add(codigoPane[i], 1, i);
            codigolabel[i].setStyle("-fx-font-size: 14px;");
            codigoPane[i].setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");

        }

        codigolabel[0].setText("public class AlgoritmoBFSGrafo {");
        codigolabel[1].setText("    private boolean[] marcado;");
        codigolabel[2].setText("    private int[] arestaPara;");
        codigolabel[3].setText("    private int[] distPara;");
        codigolabel[4].setText("    private int vo;");
        codigolabel[5].setText("");
        codigolabel[6].setText("    public AlgoritmoBFSGrafo(Grafo G, int vo){");
        codigolabel[7].setText("        this.vo = vo;");
        codigolabel[8].setText("        arestaPara = new int[G.V()];");
        codigolabel[9].setText("        marcado = new boolean[G.V()];");
        codigolabel[10].setText("       distPara = new Integer[G.V()];");
        codigolabel[11].setText("       bfs(G, vo);");
        codigolabel[12].setText("   }");
        codigolabel[13].setText("");

        codigolabel[14].setText("    private void dfs(Grafo G, int vo) {");
        codigolabel[15].setText("        Fila<Integer> f = new Fila<Integer>();");
        codigolabel[16].setText("        f.enfileira(vo);");
        codigolabel[17].setText("        marcado[vo] = true;");
        codigolabel[18].setText("        distPara[vo] = 0;");
        codigolabel[19].setText("        while (!f.isEmpty()) {");
        codigolabel[20].setText("            int v = f.desenfileira();");
        codigolabel[21].setText("            for (Aresta a : G.adj(v)) {");
        codigolabel[22].setText("                int x = a.getV2();");
        codigolabel[23].setText("                if (!marcado[x]) {");
        codigolabel[24].setText("                    arestaPara[x] = v;");
        codigolabel[25].setText("                    distPara[x] = distPara[v] + 1;");
        codigolabel[26].setText("                    marcado[x] = true;");
        codigolabel[27].setText("                    f.enfileira(x);");
        codigolabel[28].setText("                }");
        codigolabel[29].setText("            }");
        codigolabel[30].setText("        }");
        codigolabel[31].setText("    }");
        codigolabel[32].setText("}");

        for (int i = 0; i < 33; i++) {
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
    private void executarBFS() throws InterruptedException {

        System.out.println("executarBFS(()" + Thread.currentThread().getName());
        runnableBFS = new RunnableBFS(tableViewPropriedades, grafo, FXMLVBoxMainController.origem, codigoPane, componentesLine, componentesCircle, filaView, VerticeExplorando, Thread.currentThread(), variaveis);
        threadBFS = new Thread(runnableBFS);
        threadBFS.setName("Thread BFS");
        threadBFS.setDaemon(true);
        threadBFS.start();
    }

}
