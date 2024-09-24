/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos.controller;


import grafos.algoritmos.AlgoritmoMSTPrimEager;
import grafos.arquivo.In;
import static grafos.controller.FXMLExplorarComPrimLazyController.alertMsg;
import grafos.model.Desenho;
import grafos.model.GrafoPonderado;
import grafos.model.PrimEagerItemTablewViewMST;
import grafos.model.PrimEagerItemTablewViewPQ;
import grafos.model.PrimMarcado;
import grafos.runnable.RunnablePrimEager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
public class FXMLExplorarComPrimEagerController implements Initializable {

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
    public TableView<PrimEagerItemTablewViewMST> tableViewMST;
    @FXML
    private TableColumn<PrimEagerItemTablewViewMST, String> tableColumnMSTArestaPara;
    @FXML
    private TableColumn<PrimEagerItemTablewViewMST, Double> tableColumnMSTDistPara;
    
    //Tabela com as arestas de PQ
    @FXML
    public TableView<PrimEagerItemTablewViewPQ> tableViewPQ;
    @FXML
    private TableColumn<PrimEagerItemTablewViewPQ, Integer> tableColumnPQindex;
    @FXML
    private TableColumn<PrimEagerItemTablewViewPQ, Double> tableColumnPQPeso; // key
    
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
    public RunnablePrimEager runnablePrimEager;  //objeto do tipo Runnable utilizado para alterar os elementos da interface grafica. Ele é instanciado no método executarKruskal 
    public Thread threadPrimEager;             //Thread que utiliza o runnableKruskal
    public static int pausado;               //variável que marca quando a execução esta parada ou não
    public static Alert alertMsg;
   
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
            //iniciando o Prim Eager
            executarPrimEager();
        } catch (InterruptedException ex) {
            //Logger.getLogger(FXMLExplorarComDFSController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Ação do botão voltar
     * @throws IOException excepion
     */
    @FXML
    public void handleButtonVoltar() throws IOException {
        runnablePrimEager.setExecutar(false);

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

        FXMLExplorarComPrimEagerController.pausado = 1;

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

        FXMLExplorarComPrimEagerController.pausado = 0;
    }

    /**
     * Ação do botão acelerar. utilizado para aumentar a velocidade de execução do algoritmo
     * @throws IOException excepion
     * @throws InterruptedException  excepion
     */
    @FXML
    public void handleButtonAcelerar() throws IOException, InterruptedException {
        if (AlgoritmoMSTPrimEager.velocidade >= 200) {
            AlgoritmoMSTPrimEager.velocidade -= 100;
        }

    }

    /**
     * Ação do botão desacelerar. utilizado para diminuir a velocidade de execução do algoritmo
     * @throws IOException excepion
     * @throws InterruptedException excepion
     */
    @FXML
    public void handleButtonDesacelerar() throws IOException, InterruptedException {

        AlgoritmoMSTPrimEager.velocidade += 100;

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
        tableColumnMSTArestaPara.setCellValueFactory(new PropertyValueFactory<>("arestaPara"));
        tableColumnMSTDistPara.setCellValueFactory(new PropertyValueFactory<>("distPara"));
        
        tableColumnPQindex.setCellValueFactory(new PropertyValueFactory<>("i"));
        tableColumnPQPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        
        tableColumnMarcadoV.setCellValueFactory(new PropertyValueFactory<>("vertice"));
        tableColumnMarcadoMarcado.setCellValueFactory(new PropertyValueFactory<>("marcado"));
    }

    /**
     * Adiciona o código do algoritmo que o usuário esqueceu em um grid
     */
    private void adicionarCodigo() {
        codigolabel = new Label[38];
        codigoPane = new Pane[38];

        for (int i = 0; i < 38; i++) {
            codigoPane[i] = new Pane();
            codigolabel[i] = new Label();

            codigoGrid.add(codigoPane[i], 1, i);
            codigolabel[i].setStyle("-fx-font-size: 14px;");
            codigoPane[i].setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");

        }

        codigolabel[0].setText("public class AlgoritmoMSTPrimEager {");
        codigolabel[1].setText("    private Aresta[] arestaPara;");
        codigolabel[2].setText("    private double[] distanciaPara;");
        codigolabel[3].setText("    private boolean[] marcado;");
        codigolabel[4].setText("    private FilaPrioridadeMinIndex<Double> pq;");
        codigolabel[5].setText("");
        codigolabel[6].setText("    public AlgoritmoMSTPrimEager(GrafoPonderado G) {");
        codigolabel[7].setText("        arestaPara = new Aresta[G.V()];");
        codigolabel[8].setText("        distanciaPara = new double[G.V()];");
        codigolabel[9].setText("        marcado = new boolean[G.V()];");
        codigolabel[10].setText("        pq = new FilaPrioridadeMinIndex<Double>(G.V());");
        codigolabel[11].setText("        for(int v = 0; v < G.V(); v++) {");
        codigolabel[12].setText("            distanciaPara[v] = Double.POSITIVE_INFINITY;");
        codigolabel[13].setText("        }");
        codigolabel[14].setText("        distanciaPara[" + FXMLVBoxMainController.origem + "] = 0.0;");
        codigolabel[15].setText("        pq.insere(" + FXMLVBoxMainController.origem + ", distanciaPara[" + FXMLVBoxMainController.origem + "]);");
        codigolabel[16].setText("        while(!pq.isEmpty()) {");
        codigolabel[17].setText("            int v = pq.removeMin();");
        codigolabel[18].setText("            visita(G, v);");
        codigolabel[19].setText("        }");
        codigolabel[20].setText("    }");
        codigolabel[21].setText("    private void visita(GrafoPonderado G, int v) {");
        codigolabel[22].setText("        marcado[v] = true;");
        codigolabel[23].setText("        for(Aresta a : G.adj(v)) {");
        codigolabel[24].setText("            int v2 = a.outroVertice(v);");
        codigolabel[25].setText("            if(marcado[v2]) continue;");
        codigolabel[26].setText("            if(a.peso() < distanciaPara[v2]) {");
        codigolabel[27].setText("                distanciaPara[v2] = a.peso();");
        codigolabel[28].setText("                arestaPara[v2] = a;");
        codigolabel[29].setText("                if(pq.contains(v2)) {");
        codigolabel[30].setText("                    pq.diminuiChave(v2, distanciaPara[v2]);");                
        codigolabel[31].setText("                }else {");        
        codigolabel[32].setText("                    pq.insere(v2, distanciaPara[v2]);");                
        codigolabel[33].setText("                }");                
        codigolabel[34].setText("            }");               
        codigolabel[35].setText("        }");               
        codigolabel[36].setText("    }");        
        codigolabel[37].setText("}"); 
        
        for (int i = 0; i < 38; i++) {
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
    private void executarPrimEager() throws InterruptedException {
        runnablePrimEager = new RunnablePrimEager(grafo, Thread.currentThread(), componentesLine, componentesCircle, codigoPane, tableViewMST, tableViewPQ, tableViewMarcado, variaveis, FXMLVBoxMainController.origem, alertMsg);
        threadPrimEager = new Thread(runnablePrimEager);
        threadPrimEager.setName("Thread Prim Eager");
        threadPrimEager.setDaemon(true);
        threadPrimEager.start();
    }

}
