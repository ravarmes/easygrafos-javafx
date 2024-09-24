package grafos.runnable;

import grafos.algoritmos.AlgoritmoMSTPrimLazy;
import grafos.controller.FXMLExplorarComPrimLazyController;
import grafos.controller.FXMLVBoxMainController;
import grafos.model.Aresta;
import grafos.model.FilaPrioridadeMin;
import grafos.model.GrafoPonderado;
import grafos.model.PrimLazyItemTablewViewMST;
import grafos.model.PrimLazyItemTablewViewPQ;
import grafos.model.PrimMarcado;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Danielle
 */
public class RunnablePrimLazy implements Runnable {

    //Elementos gráficos que a runnable terá acesso para alterar
    static Pane[] codigoPane;
    static Group componentesLine;
    static Group componentesCircle;
    static TableView tableViewMST;
    static TableView tableViewPQ;
    static TableView tableViewMarcado;
    static Button variaveis;

    static GrafoPonderado grafo;  // Grafo que foi gerado na tela de escolha de grafo
    private Thread main;          // Thread do Javafx
    private boolean executar;     // variavel booleam que verifica se a Thread que implementa o RunnableKruskal esta ativa ou não 
    
    private Integer v0; // vertice de origem
    static Alert alertMsg;
    /**
     * Estes atributos são recebidos do controller da tela de execução do
     * KruskalItemTablewViewMST
     *
     * @param grafo grafo geraro do txt escolhido na primeira tela
     * @param main JavaFX Application Thread
     * @param componentesLine Grupo de line
     * @param componentesCircle Grupo de Circle
     * @param codigoPane Pane com código a ser colorido
     * @param tableViewMST tableView que tem as arestas escolhidas para árvores
     * geradora mínima
     * @param tableViewPQ tableView que tem todas as arestas em ordem de mínimo
     * peso
     * @param variaveis Button com valor de váriaveis importantes do algoritmo
     * @param tableViewMarcado tableView que tem indica se o vértice já foi visitado
     */
    public RunnablePrimLazy(GrafoPonderado grafo, Thread main, Group componentesLine, Group componentesCircle, 
            Pane[] codigoPane, TableView tableViewMST, TableView tableViewPQ, TableView tableViewMarcado, Button variaveis, int v0, Alert alertMsg) {

        RunnablePrimLazy.grafo = grafo;
        this.main = main;
        RunnablePrimLazy.componentesLine = componentesLine;
        RunnablePrimLazy.componentesCircle = componentesCircle;
        RunnablePrimLazy.codigoPane = codigoPane;
        RunnablePrimLazy.tableViewMST = tableViewMST;
        RunnablePrimLazy.tableViewPQ = tableViewPQ;
        RunnablePrimLazy.tableViewMarcado = tableViewMarcado;
        RunnablePrimLazy.variaveis = variaveis;
        this.executar = true;
        
        this.v0 = v0;
        this.alertMsg = alertMsg;
    }

    /**
     *
     * @return return true se a Thread esta ativa e false se não estiver
     */
    public boolean isExecutar() {
        return executar;
    }

    /**
     *
     * @param executar recebe o estado da Thread. True se for para ativa-la e
     * false se for para desativa-la
     */
    public void setExecutar(boolean executar) {
        this.executar = executar;
    }

    @Override
    public void run() {
        try {
            //Instancia um objeto do tipo AlgoritmoPrimLazy. No seu construtor o algoritmo AlgoritmoPrimLazy começa a ser realmente executado
            AlgoritmoMSTPrimLazy AlgoritmoPrimLazy = new AlgoritmoMSTPrimLazy(this.grafo, this, this.main, this.v0);
        } catch (InterruptedException ex) {
            Logger.getLogger(RunnablePrimLazy.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.arestasUtilizadas(true);
        } catch (InterruptedException ex) {
            Logger.getLogger(RunnablePrimLazy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param listKruskalItemTablewViewMST Lista com objetos do tipo
     * KruskalItemTablewViewMST, para preencher a tabela de parâmetros
     * (tableViewPropriedades)
     * @param validade true se a Thread que implementa a runnableKruskal estiver
     * ativa e false para não ativa.
     */
    public static void alterarTablewViewMST(List<PrimLazyItemTablewViewMST> listPrimLazyItemTablewViewMST, boolean validade) {
        if (validade) {
            ObservableList<PrimLazyItemTablewViewMST> ObservableListTabela = FXCollections.observableArrayList(listPrimLazyItemTablewViewMST);
            Platform.runLater(() -> {
                RunnablePrimLazy.tableViewMST.setItems(ObservableListTabela);
                RunnablePrimLazy.tableViewMST.refresh();
            });
        }
    }

    /**
     *
     * @param listKruskalItemTablewViewPQ Lista com objetos do tipo
     * KruskalItemTablewViewPQ, para preencher a tabela de parâmetros
     * (tableViewPropriedades)
     * @param validade true se a Thread que implementa a runnableKruskal estiver
     * ativa e false para não ativa.
     */
    public static void alterarTablewViewPQ(FilaPrioridadeMin<PrimLazyItemTablewViewPQ> filaPrimLazyItemTablewViewPQ, boolean validade) {
        if (validade) {
            
            List<PrimLazyItemTablewViewPQ> listPrimLazyItemTablewViewPQ = new ArrayList<PrimLazyItemTablewViewPQ>();
            for (PrimLazyItemTablewViewPQ item : filaPrimLazyItemTablewViewPQ) {
                listPrimLazyItemTablewViewPQ.add(item);
            }
            
            ObservableList<PrimLazyItemTablewViewPQ> ObservableListTabela = FXCollections.observableArrayList(listPrimLazyItemTablewViewPQ);
            Platform.runLater(() -> {
                RunnablePrimLazy.tableViewPQ.setItems(ObservableListTabela);
                RunnablePrimLazy.tableViewPQ.refresh();
            });
        }
    }

    /**
     *
     * @param listKruskalItemTablewViewPQ Lista com objetos do tipo
     * KruskalItemTablewViewPQ, para preencher a tabela de parâmetros
     * (tableViewPropriedades)
     * @param validade true se a Thread que implementa a runnableKruskal estiver
     * ativa e false para não ativa.
     */
    public static void alterarTablewMarcado(List<PrimMarcado> listMarcado, boolean validade) {
        if (validade) {

            ObservableList<PrimMarcado> ObservableListTabela = FXCollections.observableArrayList(listMarcado);
            Platform.runLater(() -> {
                RunnablePrimLazy.tableViewMarcado.setItems(ObservableListTabela);
                RunnablePrimLazy.tableViewMarcado.refresh();
            });
        }
    }

    /**
     *
     * @param status recebe um String que representa o valor das váriaveis
     * importantes do código
     * @param validade true se a Thread que implementa a runnableKruskal estiver
     * ativa e false para não ativa.
     */
    public static void exibirVariaveis(String status, boolean validade) {
        if (validade) {
            Platform.runLater(() -> {
                RunnablePrimLazy.variaveis.setText(status);
            });
        }
    }

    /**
     *
     * @param listKruskalParametros Lista com objetos do tipo
     * KruskalItemTablewViewMST, para preencher a tabela de parâmetros
     * (tableViewPropriedades)
     * @param validade true se a Thread que implementa a runnableKruskal estiver
     * ativa e false para não ativa
     */
    public static void iniciarTabela(List<PrimLazyItemTablewViewMST> listPrimLazyParametros, boolean validade) {
        if (validade) {
            ObservableList<PrimLazyItemTablewViewMST> ObservableListTabela = FXCollections.observableArrayList(listPrimLazyParametros);
            Platform.runLater(() -> {
                RunnablePrimLazy.tableViewMST.setItems(ObservableListTabela);
            });
        }
    }

    /**
     *
     * @param idAtual id da posição que contém a linha de código que deverá ser
     * colorida
     * @param idAntigo id da posição que contém a linha de código que estava
     * colorida
     * @param validade true se a Thread que implementa a runnableKruskal estiver
     * ativa e false para não ativa
     */
    public static void alterarlistCodigo(int idAtual, int idAntigo, boolean validade) {
        if (validade) {
            codigoPane[idAntigo].setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");
            codigoPane[idAtual].setStyle("-fx-background-color: #00FF7F; -fx-border-color: #000000;");
        }
    }

    /**
     * Encontra as arestas utilizadas na solução e manda para o método de
     * colorir resultado
     *
     * @param validade true se a Thread que implementa a runnableKruskal estiver
     * ativa e false para não ativa
     * @throws InterruptedException exception
     */
    public void arestasUtilizadas(boolean validade) throws InterruptedException {
        if (validade) {
            int i = 0, count = 0, j = 0;
            for (i = 0; i < grafo.V(); i++) {
                count = grafo.getAdj()[i].size();
                for (j = 0; j < count; j++) {
                    //achando a primeira aresta
                    if (grafo.getAdj()[i].get(j).isUtilizada()) {
                        RunnablePrimLazy.colorirResultado(grafo.getAdj()[i].get(j).getV1().getId(), grafo.getAdj()[i].get(j).getV2().getId(), validade);
                    }
                }
            }
        }
    }

    /**
     *
     * @param v1 vértice 1 da aresta a ser colorida
     * @param v2 vértice 2 da aresta a ser colorida
     * @param validade true se a Thread que implementa a runnableKruskal estiver
     * ativa e false para não ativa
     */
    public static void colorirResultado(int v1, int v2, boolean validade) {

        Circle circuloOrigem = (Circle) componentesCircle.getChildren().get(FXMLVBoxMainController.origem);
        circuloOrigem.setStrokeWidth(6);

        if (validade) {

            for (Aresta a : grafo.arestas()) {
                if (((a.getV1().getId() == v1) && (a.getV2().getId() == v2)) || ((a.getV1().getId() == v2) && (a.getV2().getId() == v1))) {
                    for (Node l : componentesLine.getChildren()) {
                        Line li = (Line) l;
                        if (li.getId().equals(String.valueOf(a.getId()))) {
                            Platform.runLater(() -> {
                                li.setStroke(Color.rgb(60, 195, 100));
                                li.setStrokeWidth(3);
                                colorirVertice(v1, 60, 195, 100, validade);
                                colorirVertice(v2, 60, 195, 100, validade);
                            });
                        }
                    }
                }
            }
        }

    }

    /**
     *
     * @param id Id do vértice a ser colorido
     * @param r componente de cor
     * @param g componente de cor
     * @param b componente de cor
     * @param validade true se a Thread que implementa a runnableKruskal estiver
     * ativa e false para não ativa
     */
    public static void colorirVertice(int id, int r, int g, int b, boolean validade) {
        if (validade) {

            Circle circle = (Circle) componentesCircle.getChildren().get(id);
            circle.setFill(Color.rgb(r, g, b));
        }

    }

    /**
     * 
     * @param v             vertice 1 da aresta a ser colorida
     * @param arestaPara    vertide 2 da aresta a ser colorida
     * @param r             componente da cor futura da aresta
     * @param g             componente da cor futura da aresta
     * @param b             componente da cor futura da aresta
     * @param validade      true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa
     * @param r2            componente da cor atual da aresta
     * @param g2            componente da cor atual da aresta
     * @param b2            componente da cor atual da aresta
     * @throws              InterruptedException exception
     */
    public static void colorirAresta(int v, int arestaPara, int r, int g, int b, boolean validade, int r2, int g2, int b2) throws InterruptedException {
        String cor = "";
        //verifico se a cor futura é cinza. Se for o sentido de coloração é ao contrário (do v2 para o v1)
        if (r == 211) {
            cor = "cinza";
        }
        
        Aresta aresta1[] = new Aresta[2];
        Line linhas[] = new Line[2];

        int achouAresta = 0;
        if (validade) {
            int i = 0, count = 0, j = 0;
            for (Aresta a : grafo.arestas()) {
                if (((a.getV1().getId() == v) && (a.getV2().getId() == arestaPara))) {
                    aresta1[0] = a;
                } else if (((a.getV1().getId() == arestaPara) && (a.getV2().getId() == v))) {
                    aresta1[1] = a;
                }
            }

            i = 0;
            for (Node l : componentesLine.getChildren()) {
                Line li = (Line) l;
                if (li.getId().equals(String.valueOf(aresta1[0].getId()))) {
                    linhas[0] = li;
                } else if (li.getId().equals(String.valueOf(aresta1[1].getId()))) {
                    linhas[1] = li;
                }
            }
            i = 0;
           
            double startX, stratY, endX, endY;
            double startX2, stratY2, endX2, endY2;
            //definindo o sentido
            if (cor.equals("cinza")) {
                startX = linhas[1].getStartX();
                stratY = linhas[1].getStartY();
                endX = linhas[1].getEndX();
                endY = linhas[1].getEndY();
                startX2 = linhas[0].getEndX();
                stratY2 = linhas[0].getEndY();
                endX2 = linhas[0].getStartX();
                endY2 = linhas[0].getStartY();
            } else {
                startX = linhas[0].getStartX();
                stratY = linhas[0].getStartY();
                endX = linhas[0].getEndX();
                endY = linhas[0].getEndY();
                startX2 = linhas[1].getEndX();
                stratY2 = linhas[1].getEndY();
                endX2 = linhas[1].getStartX();
                endY2 = linhas[1].getStartY();
            }
            
            //criando a task para colorir a aresta. Ele é necessária pois se fizer o sleep aqui a aplicação trava, pois vai mandar a thread do java parar
            //e isso não pode ser feito
            Task task = new Task<Void>() {
                @Override
                public Void call() throws InterruptedException {
                    linhas[1].setStrokeWidth(5);
                    linhas[0].setStrokeWidth(5);
                    double i = 0, j = 0;
                    while ((i <= 1) || (j <= 1)) {
                       //System.out.println("Teste" + i);
                        Stop[] stops = new Stop[]{new Stop(j, Color.rgb(r, g, b)), new Stop(i, Color.rgb(r2, g2, b2))};
                        LinearGradient linearGradient = new LinearGradient(startX, stratY, endX, endY, false, CycleMethod.REPEAT, stops);
                        
                        linhas[0].setStroke(linearGradient);
                        
                        Stop[] stops1 = new Stop[]{new Stop(j, Color.rgb(r, g, b)), new Stop(i, Color.rgb(r2, g2, b2) )};
                        LinearGradient linearGradient1 = new LinearGradient(startX2, stratY2, endX2, endY2, false, CycleMethod.REPEAT, stops1);  
                        
                        linhas[1].setStroke(linearGradient1);

                        if (i > 1) {
                            j += 0.01;
                        } else {
                            i += 0.01;
                        }
                        Thread.sleep(AlgoritmoMSTPrimLazy.velocidade/40);
                    }
                    return null;
                }
            };

            //O join faz a thread que invocou a task (Thread do bfs), parar e voltar somente quando a execução da task terminar
            Thread t = new Thread(task);
            t.start();
            t.join();
        }

    }
    
    public static void colorirArestaCinza(int v, int arestaPara, int r, int g, int b, boolean validade, int r2, int g2, int b2) throws InterruptedException {
        String cor = "";
        //verifico se a cor futura é cinza. Se for o sentido de coloração é ao contrário (do v2 para o v1)
        if (r == 211) {
            cor = "cinza";
        }
        
        Aresta aresta1[] = new Aresta[2];
        Line linhas[] = new Line[2];

        int achouAresta = 0;
        if (validade) {
            int i = 0, count = 0, j = 0;
            for (Aresta a : grafo.arestas()) {
                if (((a.getV1().getId() == v) && (a.getV2().getId() == arestaPara))) {
                    aresta1[0] = a;
                } else if (((a.getV1().getId() == arestaPara) && (a.getV2().getId() == v))) {
                    aresta1[1] = a;
                }
            }

            i = 0;
            for (Node l : componentesLine.getChildren()) {
                Line li = (Line) l;
                if (li.getId().equals(String.valueOf(aresta1[0].getId()))) {
                    linhas[0] = li;
                } else if (li.getId().equals(String.valueOf(aresta1[1].getId()))) {
                    linhas[1] = li;
                }
            }
            i = 0;
           
            double startX, stratY, endX, endY;
            double startX2, stratY2, endX2, endY2;
            //definindo o sentido
            if (cor.equals("cinza")) {
                startX = linhas[1].getStartX();
                stratY = linhas[1].getStartY();
                endX = linhas[1].getEndX();
                endY = linhas[1].getEndY();
                startX2 = linhas[0].getEndX();
                stratY2 = linhas[0].getEndY();
                endX2 = linhas[0].getStartX();
                endY2 = linhas[0].getStartY();
            } else {
                startX = linhas[0].getStartX();
                stratY = linhas[0].getStartY();
                endX = linhas[0].getEndX();
                endY = linhas[0].getEndY();
                startX2 = linhas[1].getEndX();
                stratY2 = linhas[1].getEndY();
                endX2 = linhas[1].getStartX();
                endY2 = linhas[1].getStartY();
            }
            
            //criando a task para colorir a aresta. Ele é necessária pois se fizer o sleep aqui a aplicação trava, pois vai mandar a thread do java parar
            //e isso não pode ser feito
            Task task = new Task<Void>() {
                @Override
                public Void call() throws InterruptedException {
                    linhas[1].setStrokeWidth(3.5);
                    linhas[0].setStrokeWidth(3.5);
                    double i = 0, j = 0;
                    while ((i <= 1) || (j <= 1)) {
                       //System.out.println("Teste" + i);
                        Stop[] stops = new Stop[]{new Stop(j, Color.rgb(r, g, b)), new Stop(i, Color.rgb(r2, g2, b2))};
                        LinearGradient linearGradient = new LinearGradient(startX, stratY, endX, endY, false, CycleMethod.REPEAT, stops);
                        
                        linhas[0].setStroke(linearGradient);
                        
                        Stop[] stops1 = new Stop[]{new Stop(j, Color.rgb(r, g, b)), new Stop(i, Color.rgb(r2, g2, b2) )};
                        LinearGradient linearGradient1 = new LinearGradient(startX2, stratY2, endX2, endY2, false, CycleMethod.REPEAT, stops1);  
                        
                        linhas[1].setStroke(linearGradient1);

                        if (i > 1) {
                            j += 0.01;
                        } else {
                            i += 0.01;
                        }
                        Thread.sleep(AlgoritmoMSTPrimLazy.velocidade/40);
                    }
                    return null;
                }
            };

            //O join faz a thread que invocou a task (Thread do bfs), parar e voltar somente quando a execução da task terminar
            Thread t = new Thread(task);
            t.start();
            t.join();
        }

    }
    
    public static void showSuccess(boolean validade) {
        if (validade) {
            Platform.runLater(() -> {
            RunnablePrimLazy.alertMsg = new Alert(Alert.AlertType.INFORMATION);
            RunnablePrimLazy.alertMsg.setHeaderText("Sucesso!");
            RunnablePrimLazy.alertMsg.setContentText("Suceeso ao encontrar a árvore geradora mínima do grafo!");
            RunnablePrimLazy.alertMsg.show();
            });
        }

    }
    
    public static void showFailure(boolean validade) {
        if (validade) {
            Platform.runLater(() -> {
            RunnablePrimEager.alertMsg = new Alert(Alert.AlertType.WARNING);
            RunnablePrimEager.alertMsg.setHeaderText("Ops!");
            RunnablePrimEager.alertMsg.setContentText("Não foi possível encontrar uma árvore geradora mínima do grafo!");
            RunnablePrimEager.alertMsg.show();
            });
        }

    }

}
