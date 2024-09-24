/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos.controller;


import grafos.algoritmos.AlgoritmoMSTPrimLazy;
import grafos.arquivo.In;
import grafos.model.Desenho;
import grafos.model.GrafoPonderado;
import grafos.model.PrimLazyItemTablewViewMST;
import grafos.model.PrimLazyItemTablewViewPQ;
import grafos.model.PrimMarcado;
import grafos.runnable.RunnablePrimLazy;
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
import javafx.scene.control.Alert;
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
 * @author Danielle
 */
public class FXMLExplorarComPrimLazyController implements Initializable {

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
    public TableView<PrimLazyItemTablewViewMST> tableViewMST;
    @FXML
    private TableColumn<PrimLazyItemTablewViewMST, String> tableColumnMSTAresta;
    
    //Tabela com as arestas de PQ
    @FXML
    public TableView<PrimLazyItemTablewViewPQ> tableViewPQ;
    @FXML
    private TableColumn<PrimLazyItemTablewViewPQ, String> tableColumnPQAresta;
    @FXML
    private TableColumn<PrimLazyItemTablewViewPQ, Double> tableColumnPQPeso;
    
    //Tabela indicando se o vértice foi visitado
    @FXML
    public TableView<PrimMarcado> tableViewMarcado;
    @FXML
    private TableColumn<PrimMarcado, Integer> tableColumnMarcadoV;
    @FXML
    private TableColumn<PrimMarcado, String> tableColumnMarcadoMarcado;
    
    private List<PrimMarcado> listMarcado;
    private ObservableList<PrimMarcado> observableListMarcado;
    
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
    public RunnablePrimLazy runnablePrimLazy;  //objeto do tipo Runnable utilizado para alterar os elementos da interface grafica. Ele é instanciado no método executarKruskal 
    public Thread threadPrimLazy;             //Thread que utiliza o runnableKruskal
    public static int pausado;               //variável que marca quando a execução esta parada ou não
    public static Alert alertMsg;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        //desenhando o grafo
        desenharGrafo();

        //inicializar a atbela de parametros do algoritmo. A atualização será feita no decorrer da execução do código
        inicializarTabelas();
        

        //Adicionado o código de Prim versão Lazy à exibição
        adicionarCodigo();

        //adicionando a lista de adjacencias para exibição
        carregarListaDeAdjacencias();

        try {
            //iniciando o Prim Lazy
            executarPrimLazy();
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
        runnablePrimLazy.setExecutar(false);

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

        FXMLExplorarComPrimLazyController.pausado = 1;

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

        FXMLExplorarComPrimLazyController.pausado = 0;
    }

    /**
     * Ação do botão acelerar. utilizado para aumentar a velocidade de execução do algoritmo
     * @throws IOException excepion
     * @throws InterruptedException  excepion
     */
    @FXML
    public void handleButtonAcelerar() throws IOException, InterruptedException {
        if (AlgoritmoMSTPrimLazy.velocidade >= 200) {
            AlgoritmoMSTPrimLazy.velocidade -= 100;
        }

    }

    /**
     * Ação do botão desacelerar. utilizado para diminuir a velocidade de execução do algoritmo
     * @throws IOException excepion
     * @throws InterruptedException excepion
     */
    @FXML
    public void handleButtonDesacelerar() throws IOException, InterruptedException {

        AlgoritmoMSTPrimLazy.velocidade += 100;

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
        
        tableColumnMarcadoV.setCellValueFactory(new PropertyValueFactory<>("vertice"));
        tableColumnMarcadoMarcado.setCellValueFactory(new PropertyValueFactory<>("marcado"));
    }

    /**
     * Adiciona o código do algoritmo que o usuário escolheu em um grid
     */
    private void adicionarCodigo() {
        codigolabel = new Label[28];
        codigoPane = new Pane[28];

        for (int i = 0; i < 28; i++) {
            codigoPane[i] = new Pane();
            codigolabel[i] = new Label();

            codigoGrid.add(codigoPane[i], 1, i);
            codigolabel[i].setStyle("-fx-font-size: 14px;");
            codigoPane[i].setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");

        }

        codigolabel[0].setText("public class AlgoritmoMSTPrimLazy {");
        codigolabel[1].setText("    private Fila<Aresta> mst;");
        codigolabel[2].setText("    private boolean[] marcado;");
        codigolabel[3].setText("    private FilaPrioridadeMin<Aresta> pq;");
        codigolabel[4].setText("");
        codigolabel[5].setText("    public AlgoritmoMSTPrimLazy(GrafoPonderado G) {");
        codigolabel[6].setText("        mst = new Fila<Aresta>();");
        codigolabel[7].setText("        pq = new FilaPrioridadeMin<Aresta>();");
        codigolabel[8].setText("        marcado = new boolean[G.V()];");
        codigolabel[9].setText("        visita(G, " + FXMLVBoxMainController.origem + ");");
        codigolabel[10].setText("       while(!pq.isEmpty()) {");
        codigolabel[11].setText("            Aresta a = pq.delMin();");
        codigolabel[12].setText("            int v1 = a.V1(), v2 = a.V2();");
        codigolabel[13].setText("            if(marcado[v1] && marcado[v2]) continue;");
        codigolabel[14].setText("            mst.enfileira(a);");
        codigolabel[15].setText("            if(!marcado[v1]) visita(G, v1);");
        codigolabel[16].setText("            if(!marcado[v2]) visita(G, v2);");
        codigolabel[17].setText("        }");
        codigolabel[18].setText("    }");
        codigolabel[19].setText("    private void visita(GrafoPonderado G, int v) {");
        codigolabel[20].setText("        marcado[v] = true;");
        codigolabel[21].setText("        for(Aresta a : G.adj(v)) {");
        codigolabel[22].setText("            if(!marcado[a.outroVertice(v)]) {");
        codigolabel[23].setText("                pq.insere(a);");
        codigolabel[24].setText("            }");
        codigolabel[25].setText("        }");
        codigolabel[26].setText("    }");
        codigolabel[27].setText("}");

        for (int i = 0; i < 28; i++) {
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
    private void executarPrimLazy() throws InterruptedException {
        runnablePrimLazy = new RunnablePrimLazy(grafo, Thread.currentThread(), componentesLine, componentesCircle, codigoPane, tableViewMST, tableViewPQ, tableViewMarcado, variaveis, FXMLVBoxMainController.origem, alertMsg);
        threadPrimLazy = new Thread(runnablePrimLazy);
        threadPrimLazy.setName("Thread Prim Lazy");
        threadPrimLazy.setDaemon(true);
        threadPrimLazy.start();
    }
}
