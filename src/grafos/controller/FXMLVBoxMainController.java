/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos.controller;

import grafos.algoritmos.AlgoritmoBFS;
import grafos.algoritmos.AlgoritmoDFS;
import grafos.arquivo.In;
import grafos.model.Desenho;
import grafos.model.Grafo;
import grafos.model.GrafoPonderado;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author john_
 */
public class FXMLVBoxMainController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private MenuItem menuItemProcessosExplorarGrafo;
    @FXML
    private AnchorPane anchorPaneGrafo;
    @FXML
    private ComboBox comboBoxGrafos;
    @FXML
    private ComboBox comboBoxAlgoritmos;
    @FXML
    private Button buttonIniciar;
    @FXML
    private Button buttonBuscarGrafo;
    @FXML
    private Button buttonSobre;
    @FXML
    private Button buttonAlgoritmoDescr;
    @FXML
    private Button buttonAjudaBuscaGrafo;
    @FXML
    private ListView listViewCodigo;
    @FXML
    private TextField TextFieldVerticeOrigem;
    @FXML
    private TextField TextFieldVerticeDestino;
    @FXML
    private Label labelAlgoritmoDescr;
    @FXML
    private TextArea textAreaAlgoritmoDescr;
    
    private List<String> listAlgoritmos = new ArrayList<String>();
    private List<String> listGrafos = new ArrayList<String>();
    private List<String> listCodigo = new ArrayList<String>();
    private ObservableList<String> observableListAlgoritmos;
    private ObservableList<String> observableListGrafos;
    private ObservableList<String> observableListCodigo;
    public static File arquivoGrafo;

    public static int origem, destino;
    public static String msgArquivoInvalido;
    //Group componentesLine = new Group();
    //Group componentesCircle = new Group();
    //Group componentesText = new Group();

    // joguei a declaração aqui pra cima porque vou precisar utilizar no handleButtonIniciar também
    GrafoPonderado grafoPonderadoDesenho;
    Grafo grafoDesenho;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        listViewCodigo.setVisible(false);
        //new JMetro(JMetro.Style.LIGHT).applyTheme(buttonIniciar);
        //new JMetro(JMetro.Style.LIGHT).applyTheme(comboBoxAlgoritmos);
        //new JMetro(JMetro.Style.LIGHT).applyTheme(buttonBuscarGrafo);
        //new JMetro(JMetro.Style.LIGHT).applyTheme(TextFieldVerticeOrigem);
        //new JMetro(JMetro.Style.LIGHT).applyTheme(TextFieldVerticeDestino);

        TextFieldVerticeOrigem.setOnMousePressed(evt -> {
            TextFieldVerticeOrigem.setText("");
        });

        TextFieldVerticeDestino.setOnMousePressed(evt -> {
            TextFieldVerticeDestino.setText("");
        });

        listAlgoritmos.add("DFS");
        listAlgoritmos.add("BFS");
        listAlgoritmos.add("Algoritmo de Kruskal");
        listAlgoritmos.add("Algoritmo de Prim - Lazy");
        listAlgoritmos.add("Algoritmo de Prim - Eager");
        listAlgoritmos.add("Algoritmo de Dijkstra");
        listAlgoritmos.add("Algoritmo de Bellman-Ford");

        observableListAlgoritmos = FXCollections.observableArrayList(listAlgoritmos);
        comboBoxAlgoritmos.setItems(observableListAlgoritmos);

        TextFieldVerticeOrigem.setPromptText("Vértice de Origem");
        TextFieldVerticeDestino.setPromptText("Vértice de Destino");
        TextFieldVerticeOrigem.setText("Vértice de Origem");
        TextFieldVerticeDestino.setText("Vértice de Destino");

        TextFieldVerticeOrigem.setDisable(true);
        TextFieldVerticeDestino.setDisable(true);

        tooltipButtonAjudaBuscaGrafo();
        
        // DropShadow ds = new DropShadow();
        //ds.setSpread(0.1);
        //TextFieldVerticeOrigem.setEffect(ds);
        // TextFieldVerticeDestino.setEffect(ds);
        //buttonIniciar.setEffect(new InnerShadow());
        //buttonBuscarGrafo.setEffect(new InnerShadow());
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    private void centralizarText(Text text, Circle circle) {
        double W = text.getBoundsInLocal().getWidth();
        double H = text.getBoundsInLocal().getHeight();
        text.relocate(circle.getCenterX() - W / 2, circle.getCenterY() - H / 2);
    }

    @FXML
    public void handleButtonCarregarCodigo() throws IOException, InterruptedException {
        String algoritmo = comboBoxAlgoritmos.getSelectionModel().getSelectedItem().toString();

        if (!algoritmo.equals("DFS") && !algoritmo.equals("BFS") && !algoritmo.equals("Algoritmo de Kruskal")
                && !algoritmo.equals("Algoritmo de Prim - Lazy") && !algoritmo.equals("Algoritmo de Prim - Eager")) {
            listViewCodigo.setVisible(false);
            textAreaAlgoritmoDescr.setVisible(false); // para não mostrar a descrição de algum algoritmo que foi implementado
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Algoritmo não implementado!");
            alert.setContentText("Tente outro algoritmo!");
            alert.show();
        } else {

            if (algoritmo.equals("Algoritmo de Kruskal")) {
                TextFieldVerticeOrigem.setDisable(true);
                TextFieldVerticeDestino.setDisable(true);
                listCodigo = new ArrayList<String>();
                listCodigo.add("public AlgoritmoMSTKruskal {");
                listCodigo.add("    private double peso;");
                listCodigo.add("    private Fila<Aresta> mst = new Fila<Aresta>();");
                listCodigo.add("");
                listCodigo.add("    public AlgoritmoMSTKruskal(Grafo G) {");
                listCodigo.add("            FilaMin<Aresta> pq = new FilaMin<Aresta>();");
                listCodigo.add("            for (Aresta e : G.arestas()) {");
                listCodigo.add("                    pq.insere(e);");
                listCodigo.add("            }");
                listCodigo.add("            UF uf = new UF(G.V());");
                listCodigo.add("            while (!pq.isEmpty()) {");
                listCodigo.add("                    Aresta a = pq.delMin();");
                listCodigo.add("                    int v1 = a.umVertice().getId();");
                listCodigo.add("                    int v2 = a.outroVertice(a.umVertice()).getId();");
                listCodigo.add("                    if (!uf.conectado(v1, v2)) {");
                listCodigo.add("                            uf.junta(v1, v2);");
                listCodigo.add("                            mst.enfileira(a);");
                listCodigo.add("                            peso += a.getPeso();");
                listCodigo.add("                    }");
                listCodigo.add("            }");
                listCodigo.add("    }");
                //listCodigo.add("");
                listCodigo.add("}");

                adicionaDescrAlgoritmos("Foi publicado por Joseph Kruskal em 1956. Cresce uma floresta geradora até se tornar uma árvore conexa."
                        + "\n\n"
                        + "*Elege em cada etapa, considerando o menor valor de peso, uma aresta, que será acrescentada à solução final.\n"
                        + "*São adicionadas à solução, somente as arestas que não formarem ciclos com as arestas já incluídas nas etapas anteriores.\n"
                        + "*Quando todos os nós do grafo forem abordados, o algoritmo é finalizado.");
                
                observableListCodigo = FXCollections.observableArrayList(listCodigo);
                listViewCodigo.setItems(observableListCodigo);
                listViewCodigo.setStyle("-fx-font-size: 11pt;");
            } else if (algoritmo.equals("Algoritmo de Prim - Lazy")) {
                TextFieldVerticeOrigem.setDisable(false);
                TextFieldVerticeDestino.setDisable(true);
                listCodigo = new ArrayList<String>();
                listCodigo.add("public class AlgoritmoMSTPrimLazy {");
                listCodigo.add("    private Fila<Aresta> mst;");
                listCodigo.add("    private boolean[] marcado;");
                listCodigo.add("    private FilaPrioridadeMin<Aresta> pq;");
                listCodigo.add("");
                listCodigo.add("    public AlgoritmoMSTPrimLazy(GrafoPonderado G) {");
                listCodigo.add("        mst = new Fila<Aresta>();");
                listCodigo.add("        pq = new FilaPrioridadeMin<Aresta>();");
                listCodigo.add("        marcado = new boolean[G.V()];");
                listCodigo.add("        visita(G, 0)");
                listCodigo.add("        while(!pq.isEmpty()) {");
                listCodigo.add("            Aresta a = pq.delMin();");
                listCodigo.add("            int v1 = a.V1(), v2 = a.V2();");
                listCodigo.add("            if(marcado[v1] && marcado[v2]) continue;");
                listCodigo.add("            mst.enfileira(a);");
                listCodigo.add("            if(!marcado[v1]) visita(G, v1);");
                listCodigo.add("            if(!marcado[v2]) visita(G, v2);");
                listCodigo.add("        }");
                listCodigo.add("    }");
                listCodigo.add("    private void visita(GrafoPonderado G, int v) {");
                listCodigo.add("        marcado[v] = true;");
                listCodigo.add("        for(Aresta a : G.adj(v)) {");
                listCodigo.add("            if(!marcado[a.outroVertice(v)]) {");
                listCodigo.add("                pq.insere(a);");
                listCodigo.add("            }");
                listCodigo.add("        }");
                listCodigo.add("    }");
                listCodigo.add("}");
                
                adicionaDescrAlgoritmos("Foi publicado por Robert C. Prim em 1957. O algoritmo de Prim cultiva uma "
                        + "subárvore de G até que ela se torne geradora. Ou seja, a árvore geradora mínima começa a ser calculada a partir de um vértice inicial. "
                        + "Tem duas versões: Lazy e Eager."
                        + "\n\n" 
                        + "Lazy:\n "
                        + "*Mais lento.\n "
                        + "*Ao visitar um vértice armazena todas as arestas, sem se preocupar se irão ficar obsoletas.\n "
                        + "*Ocupa mais memória.");
                
                observableListCodigo = FXCollections.observableArrayList(listCodigo);
                listViewCodigo.setItems(observableListCodigo);
                listViewCodigo.setStyle("-fx-font-size: 11pt;");
            } else if (algoritmo.equals("Algoritmo de Prim - Eager")) {
                TextFieldVerticeOrigem.setDisable(false);
                TextFieldVerticeDestino.setDisable(true);
                listCodigo = new ArrayList<String>();
                listCodigo.add("public class AlgoritmoMSTPrimEager {");
                listCodigo.add("    private Aresta[] arestaPara;");
                listCodigo.add("    private double[] distanciaPara;");
                listCodigo.add("    private boolean[] marcado;");
                listCodigo.add("    private FilaPrioridadeMinIndex<Double> pq;");
                listCodigo.add("    public AlgoritmoMSTPrimEager(GrafoPonderado G) {");
                listCodigo.add("        arestaPara = new Aresta[G.V()];");
                listCodigo.add("        distanciaPara = new double[G.V()];");
                listCodigo.add("        marcado = new boolean[G.V()];");
                listCodigo.add("        pq = new FilaPrioridadeMinIndex<Double>(G.V());");
                listCodigo.add("        for(int v = 0; v < G.V(); v++) {");
                listCodigo.add("            distanciaPara[v] = Double.POSITIVE_INFINITY;");
                listCodigo.add("        }");
                listCodigo.add("        distanciaPara[0] = 0.0;");
                listCodigo.add("        pq.insere(0, distanciaPara[0]);");
                listCodigo.add("        while(!pq.isEmpty()) {");
                listCodigo.add("            int v = pq.removeMin();");
                listCodigo.add("            visita(G, v);");
                listCodigo.add("        }");
                listCodigo.add("    }");
                listCodigo.add("    private void visita(GrafoPonderado G, int v) {");
                listCodigo.add("        marcado[v] = true;");
                listCodigo.add("        for(Aresta a : G.adj(v)) {");
                listCodigo.add("            int v2 = a.outroVertice(v);");
                listCodigo.add("            if(marcado[v2]) continue;");
                listCodigo.add("            if(a.peso() < distanciaPara[v2]) {");
                listCodigo.add("                distanciaPara[v2] = a.peso();");
                listCodigo.add("                arestaPara[v2] = a;");
                listCodigo.add("                if(pq.contains(v2)) {");
                listCodigo.add("                    pq.diminuiChave(v2, distanciaPara[v2]);");
                listCodigo.add("                }else {");
                listCodigo.add("                    pq.insere(v2, distanciaPara[v2]);");
                listCodigo.add("                }");
                listCodigo.add("            }");
                listCodigo.add("        }");
                listCodigo.add("    }");
                listCodigo.add("}");
                
                adicionaDescrAlgoritmos("Foi publicado por Robert C. Prim em 1957. O algoritmo de Prim cultiva uma "
                        + "subárvore de G até que ela se torne geradora. Ou seja, a árvore geradora mínima começa a ser calculada a partir de um vértice inicial. "
                        + "Tem duas versões: Lazy e Eager."
                        + "\n\n" 
                        + "Eager:\n "
                        + "*Trabalha com programação dinâmica.\n "
                        + "*Nunca armazena mais de uma aresta para um mesmo destino.\n "
                        + "*Ocupa menos memória.");
                
                observableListCodigo = FXCollections.observableArrayList(listCodigo);
                listViewCodigo.setItems(observableListCodigo);
                listViewCodigo.setStyle("-fx-font-size: 11pt;");
            } else if (algoritmo.equals("Algoritmo de Dijkstra") || algoritmo.equals("Algoritmo de Bellman-Ford")) {
                TextFieldVerticeDestino.setDisable(false);
            } else if (algoritmo.equals("DFS")) {
                TextFieldVerticeOrigem.setDisable(false);
                TextFieldVerticeDestino.setDisable(true);
                listCodigo = new ArrayList<String>();
                listCodigo.add("public class AlgoritmoDFSGrafo {");
                listCodigo.add("    private booblean[] marcado;");
                listCodigo.add("    private int[] arestaPara;");
                listCodigo.add("");
                listCodigo.add("    public AlgoritmoDFSGrafo(Grafo G, int vo){");
                listCodigo.add("            this.vo = vo;");
                listCodigo.add("            arestaPara = new int[G.V()];");
                listCodigo.add("            marcado = new boolean[G.V()];");
                listCodigo.add("            dfs(G, vo);");
                listCodigo.add("    }");
                listCodigo.add("");
                listCodigo.add("    private void dfs(Grafo G, int v) {");
                listCodigo.add("            marcado[v] = true;");
                listCodigo.add("            for (Aresta a : G.adj(v)) {");
                listCodigo.add("                    int x = a.getV2();");
                listCodigo.add("                    if (!marcado[x]) {");
                listCodigo.add("                            arestaPara[x] = v;");
                listCodigo.add("                            dfs(G, x);");
                listCodigo.add("                    }");
                listCodigo.add("            }");
                listCodigo.add("    }");
                listCodigo.add("}");

                adicionaDescrAlgoritmos("Busca em profundidade (DFS = Depth-First Search). Tem como objetivo encontrar, partindo de um vértice origem "
                        + "qualquer, todos os vértices acessíveis."
                        + "\n\n"
                        + "*Busca sempre o vértice mais profundo.\n"
                        + "*Avança na exploração sempre considerando uma aresta que conecta o vértice atual a um vértice ainda não visitado.\n"
                        + "*Só para de \"aprofundar\" no grafo quando o vértice explorado não possui arestas para outros vértices.");
                
                observableListCodigo = FXCollections.observableArrayList(listCodigo);
                listViewCodigo.setItems(observableListCodigo);
                listViewCodigo.setStyle("-fx-font-size: 11pt;");
            } else if (algoritmo.equals("BFS")) {
                TextFieldVerticeOrigem.setDisable(false);
                TextFieldVerticeDestino.setDisable(true);
                listCodigo = new ArrayList<String>();
                listCodigo.add("public class AlgoritmoBFSGrafo {");
                listCodigo.add("    private boolean[] marcado;");
                listCodigo.add("    private int[] arestaPara;");
                listCodigo.add("    private int[] distPara;");
                listCodigo.add("    private int v;");
                listCodigo.add("");
                listCodigo.add("    public AlgoritmoBFSGrafo(Grafo G, int v){");
                listCodigo.add("        this.v = v;");
                listCodigo.add("        arestaPara = new int[G.V()];");
                listCodigo.add("        marcado = new boolean[G.V()];");
                listCodigo.add("       distPara = new Integer[G.V()];");
                listCodigo.add("       bfs(G, v);");
                listCodigo.add("   }");
                listCodigo.add("");
                listCodigo.add("    private void dfs(Grafo G, int v) {");
                listCodigo.add("        Fila<Integer> f = new Fila<Integer>();");
                listCodigo.add("        f.enfileira(vo);");
                listCodigo.add("        marcado[vo] = true;");
                listCodigo.add("        distPara[vo] = 0;");
                listCodigo.add("        while (!f.isEmpty()) {");
                listCodigo.add("            int v = f.desenfileira();");
                listCodigo.add("            for (Aresta a : G.adj(v)) {");
                listCodigo.add("                int x = a.getV2();}");
                listCodigo.add("                if (!marcado[x]) {");
                listCodigo.add("                    arestaPara[x] = v;}");
                listCodigo.add("                    distPara[x] = distPara[v] + 1;");
                listCodigo.add("                    marcado[x] = true;");
                listCodigo.add("                    f.enfileira(x);");
                listCodigo.add("                }");
                listCodigo.add("            }");
                listCodigo.add("        }");
                listCodigo.add("    }");
                listCodigo.add("}");
                
                adicionaDescrAlgoritmos("Busca em largura (BFS = Breadth-First Search). Tem como objetivo encontrar, partindo de um vértice origem "
                        + "qualquer, todos os vértices acessíveis."
                        + "\n\n"
                        + "*Explora os vértices de nível em nível.\n"
                        + "*Este tipo de busca é caracterizado por visitar todos os vértices a uma distância k do vértice de origem antes de visitar "
                        + "os que estão a uma distância k + 1.\n"
                        + "*Um vértice é finalizado quando todas as suas arestas já foram visitadas. Nesse momento a exploração no nível k se encerra dando "
                        + "início a exploração no nível k + 1.");
                
                observableListCodigo = FXCollections.observableArrayList(listCodigo);
                listViewCodigo.setItems(observableListCodigo);
                listViewCodigo.setStyle("-fx-font-size: 11pt;");
            }
            listViewCodigo.setVisible(true);
        }
    }

    @FXML
    public void handleButtonIniciar() throws IOException {
        String algoritmo = comboBoxAlgoritmos.getSelectionModel().getSelectedItem().toString();
        boolean verticeInvalido = false;
        if (arquivoGrafo == null || comboBoxAlgoritmos.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Campos obrigatórios não preenchidos!!");
            alert.setContentText("Verifique se algum campo não foi preechido da forma correta!");
            alert.show();
        } else {
            // verifica se não é nulo o ou tem o tamanho 0
            // observei que se clicava para adicionar o valor e desistia da ação, o sistema lançava uma exceção
            if(TextFieldVerticeOrigem.getText() != null && TextFieldVerticeOrigem.getText().length() > 0){
                if (!TextFieldVerticeOrigem.getText().equals("Vértice de Origem") && !TextFieldVerticeDestino.getText().equals("Vértice de Destino")) {
                    destino = Integer.parseInt(TextFieldVerticeDestino.getText());
                    origem = Integer.parseInt(TextFieldVerticeOrigem.getText());
                } else if (!TextFieldVerticeOrigem.getText().equals("Vértice de Origem")) {
                    origem = Integer.parseInt(TextFieldVerticeOrigem.getText());
                    //System.out.println("DFS ou BFS " + origem);
                    
                    if(grafoPonderadoDesenho != null){
                        if(grafoPonderadoDesenho.verificaVertice(origem) == false) verticeInvalido = true;
                    }
                }
            }

            if(verticeInvalido == false){
                if (algoritmo.equals("DFS")) {
                    AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/grafos/view/FXMLExplorarComDFS.fxml"));
                    anchorPane.getChildren().setAll(a);
                } else if (algoritmo.equals("BFS")) {
                    AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/grafos/view/FXMLExplorarComBFS.fxml"));
                    anchorPane.getChildren().setAll(a);
                } else if (algoritmo.equals("Algoritmo de Kruskal")) {
                    AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/grafos/view/FXMLExplorarComKruskal.fxml"));
                    anchorPane.getChildren().setAll(a);
                } else if (algoritmo.equals("Algoritmo de Prim - Lazy")) {
                    AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/grafos/view/FXMLExplorarComPrimLazy.fxml"));
                    anchorPane.getChildren().setAll(a);
                } else if (algoritmo.equals("Algoritmo de Prim - Eager")) {
                    AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/grafos/view/FXMLExplorarComPrimEager.fxml"));
                    anchorPane.getChildren().setAll(a);
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Vértice Inválido!");
                alert.setContentText("O vértice " + origem + " não existe no grafo selecionado!");
                alert.show();
            }
        }

        AlgoritmoDFS.velocidade = 1000;
        AlgoritmoBFS.velocidade = 1000;

    }

    @FXML
    public void handleButtonBuscarGrafo() throws IOException {

        Group componentesLine = new Group();
        Group componentesCircle = new Group();
        Group componentesText = new Group();

        anchorPaneGrafo.getChildren().clear();

        Desenho criarGrafo = new Desenho();
        FileChooser leitor = new FileChooser();
        leitor.getExtensionFilters().addAll(// isso fará com que apenas arquivos txt fiquem disponíveis para serem selecionados
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        leitor.setTitle("Escolha o arquivo do Grafo");
        arquivoGrafo = leitor.showOpenDialog(null);

        String tipoGrafo = new String();

        if (arquivoGrafo != null) { // caso fechar o explorador de arquivo não gera exceção
            In in = new In(arquivoGrafo);
            tipoGrafo = in.readString().toUpperCase();// isso vai ler a primeira linha do arquivo
            if(tipoGrafo.equals("GRAFO")){
                grafoDesenho = new Grafo(in);
                criarGrafo.desenharGrafo(grafoDesenho, anchorPaneGrafo, componentesLine, componentesCircle, componentesText);
            }
            else if(tipoGrafo.equals("GRAFOPONDERADO")){
                grafoPonderadoDesenho = new GrafoPonderado(in);
                if(msgArquivoInvalido == null){
                    criarGrafo.desenharGrafo(grafoPonderadoDesenho, anchorPaneGrafo, componentesLine, componentesCircle, componentesText);
                }else{
                    arquivoGrafo = null;
                    grafoPonderadoDesenho = null;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Arquivo Inválido!");
                    alert.setContentText(msgArquivoInvalido);
                    alert.show();
                    msgArquivoInvalido = null;
                }  
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Arquivo Inválido!");
                alert.setHeaderText("Ops... Parece que esse arquivo não está configurado corretamente!");
                alert.setContentText("Lembre-se de indicar o tipo de grafo na primeira linha do arquivo! (GRAFO ou GRAFOPONDERADO)");
                alert.show();
            }
//            if (!arquivoGrafo.getName().equals("GrafoPonderado1.txt")) {
//                Grafo grafoDesenho;
//                In in = new In(arquivoGrafo);
//                grafoDesenho = new Grafo(in);
//                //System.out.print(grafoDesenho.A());
//                //System.out.print(grafoDesenho.V());
//                //System.out.print(grafoDesenho.getAdj()[0]);
//                criarGrafo.desenharGrafo(grafoDesenho, anchorPaneGrafo, componentesLine, componentesCircle, componentesText);
//            } else {
//                GrafoPonderado grafoDesenho;
//                In in = new In(arquivoGrafo);
//                grafoDesenho = new GrafoPonderado(in);
//                //System.out.print(grafoDesenho.A());
//                //System.out.print(grafoDesenho.V());
//                //System.out.print(grafoDesenho.getAdj()[0]);
//                criarGrafo.desenharGrafo(grafoDesenho, anchorPaneGrafo, componentesLine, componentesCircle, componentesText);
//            }
        }
    }
    
    @FXML
    public void handleButtonSobre() throws IOException{
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLSobreController.class.getResource("/grafos/view/FXMLSobre.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Sobre");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);

        dialogStage.centerOnScreen();
        
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        
        FXMLSobreController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();
    }
    
    public void adicionaDescrAlgoritmos(String descr){
        textAreaAlgoritmoDescr.setVisible(true);
        textAreaAlgoritmoDescr.setWrapText(true); // quebra de linha automática
        textAreaAlgoritmoDescr.setText(descr);
    }
    
    @FXML
    public void handleButtonAjudaBuscaGrafo() throws IOException{
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAjudaBuscaGrafoController.class.getResource("/grafos/view/FXMLAjudaBuscaGrafo.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Ajuda");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);

        dialogStage.centerOnScreen();
        
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        
        FXMLAjudaBuscaGrafoController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();
    }
    
    public void tooltipButtonAjudaBuscaGrafo() {
        Tooltip tt = new Tooltip();
        tt.setText("Ajuda");
        tt.setStyle("-fx-font: normal 13 Langdon;"
                + "-fx-background-color:  #ffffff; "
                + "-fx-text-fill: Black;");

        buttonAjudaBuscaGrafo.setTooltip(tt);
    }
}
