/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos.runnable;

import grafos.algoritmos.AlgoritmoDFS;
import grafos.controller.FXMLVBoxMainController;
import grafos.model.Aresta;
import grafos.model.DFS;
import grafos.model.Grafo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
 * @author Jonathas
 */
public class RunnableDFS implements Runnable {

    //Elementos gráficos que a runnable terá acesso para alterar
    static ListView listViewPilha;
    static TableView tableViewPropriedades;
    static Pane[] codigoPane;
    static Group componentesLine;
    static Group componentesCircle;
    static Button VerticeExplorando;
    static Button variaveis;

    
    static Grafo grafo;         //grafo que foi gerado na tela de escolha de grafo
    private int v;              //vértice de origem
    private boolean executar;   // variavel booleam que verifica se a Thread que implementa o RunnableBFS esta ativa ou não 
    private Thread main;        //Thread do Javafx

    /**
     * Estes atributos são recebidos do controller da tela de execução do BFS
     * 
     * @param tableViewPropriedades     tableView que tem os valores dos vetores do código escolhido
     * @param grafo                     grafo geraro do txt escolhido na primeira tela
     * @param v                         vértice de origem
     * @param codigoPane                Pane com código a ser colorido  
     * @param componentesLine           Grupo de line
     * @param componentesCircle         Grupo de Circle
     * @param listViewPilha                  ListView com a fila de ordem de execução do algoritmo BFS
     * @param VerticeExplorando         Button com valor do vértice que esta sendo explorado
     * @param main                      JavaFX Application Thread
     * @param variaveis                 Button com valor de váriaveis importantes do algoritmo
     */
    public RunnableDFS(ListView listViewPilha, TableView tableViewPropriedades, Grafo grafo, int v, Pane[] codigoPane, Group componentesLine, Group componentesCircle, Button VerticeExplorando, Thread main, Button variaveis) {

        
        RunnableDFS.listViewPilha = listViewPilha;
        RunnableDFS.tableViewPropriedades = tableViewPropriedades;
        RunnableDFS.codigoPane = codigoPane;
        RunnableDFS.componentesLine = componentesLine;
        RunnableDFS.componentesCircle = componentesCircle;
        RunnableDFS.VerticeExplorando = VerticeExplorando;
        RunnableDFS.variaveis = variaveis; 
        
        this.main = main;

        this.executar = true;

        this.grafo = grafo;
        this.v = v;

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
     * @param executar recebe o estado da Thread. True se for para ativa-la e false se for para desativa-la
     */
    public void setExecutar(boolean executar) {
        this.executar = executar;
    }

    @Override
    public void run() {

        try {
            AlgoritmoDFS algoritmoDFS = new AlgoritmoDFS(this.v, this.grafo, this, this.main);

            this.arestasUtilizadas(true);
        } catch (InterruptedException ex) {
            Logger.getLogger(RunnableDFS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param status    Rece uma string que representa o id do vértice que esta sendo explorado
     * @param validade  true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa.
     */
    public static void exibirVerticeExplorando(String status, boolean validade) {
        if (validade) {
            Platform.runLater(() -> {

                RunnableDFS.VerticeExplorando.setText(status);

            });
        }

    }
    
    /**
     * 
     * @param status recebe um String que representa o valor das váriaveis importantes do código
     * @param validade true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa.
     */
    public static void exibirVariaveis(String status, boolean validade) {
        if (validade) {
            Platform.runLater(() -> {

                RunnableDFS.variaveis.setText(status);

            });
        }

    }

    /**
     *
     * @param listDFSParametros Lista com objetos do tipo BFS, para preencher a tabela de parâmetros (tableViewPropriedades)
     * @param validade          true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa. 
     * @throws                  java.lang.InterruptedException exception
     *  
     */
    public static void alterarTabela(List<DFS> listDFSParametros, boolean validade) throws InterruptedException {

        if (validade) {
            ObservableList<DFS> ObservableListTabela = FXCollections.observableArrayList(listDFSParametros);

            Platform.runLater(() -> {
                RunnableDFS.tableViewPropriedades.setItems(ObservableListTabela);
                RunnableDFS.tableViewPropriedades.refresh();

            });
        }
    }

    /**
     *
     * @param listDFSParametros     Lista com objetos do tipo BFS, para preencher a tabela de parâmetros (tableViewPropriedades)
     * @param validade              true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa
     */
    public static void iniciarTabela(List<DFS> listDFSParametros, boolean validade) {
        if (validade) {
            ObservableList<DFS> ObservableListTabela = FXCollections.observableArrayList(listDFSParametros);

            Platform.runLater(() -> {
                RunnableDFS.tableViewPropriedades.setItems(ObservableListTabela);
            });
        }

    }

    /**
     *
     * @param listRecursiva lista de inteiros com os novos valores para a pilha que representa a ordem de execução do algoritmo BFS
     * @param validade true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa.
     */
    public static void alterarPilhaRecursiva(List<String> listRecursiva, boolean validade) {
        if (validade) {
            ObservableList<String> ObservableListRecursiva = FXCollections.observableArrayList(listRecursiva);

            Platform.runLater(() -> {
                RunnableDFS.listViewPilha.setItems(ObservableListRecursiva);
            });
        }
    }

    
    /**
     *
     * @param listRecursiva Rece uma string que representa o id do vértice que terminou de ser explorado e deve ser retirado da pilha
     * @param validade true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa.
     */
    public static void desempilharPilhaRecursiva(List<String> listRecursiva, boolean validade) {
        //int size = listRecursiva.size();

        if (validade) {

            listRecursiva.remove(0);
            ObservableList<String> ObservableListRecursiva = FXCollections.observableArrayList(listRecursiva);

            Platform.runLater(() -> {
                RunnableDFS.listViewPilha.setItems(ObservableListRecursiva);
            });
        }

    }

    
    /**
     *
     * @param idAtual   id da posição que contém a linha de código que deverá ser colorida
     * @param idAntigo  id da posição que contém a linha de código que estava colorida
     * @param validade  true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa
     */
    public static void alterarlistCodigo(int idAtual, int idAntigo, boolean validade) {
        if (validade) {
            codigoPane[idAntigo].setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #000000;");
            codigoPane[idAtual].setStyle("-fx-background-color: #00FF7F; -fx-border-color: #000000;");
        }

    }

    
    /**
     * Encontra as arestas utilizadas na solução e manda para o método de colorir resultado
     * @param   validade true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa
     * @throws  InterruptedException exception
     */
    public void arestasUtilizadas(boolean validade) throws InterruptedException {
        if (validade) {
            int i = 0, count = 0, j = 0;
            for (i = 0; i < grafo.V(); i++) {
                count = grafo.getAdj()[i].size();
                for (j = 0; j < count; j++) {
                    //achando a primeira aresta
                    if (grafo.getAdj()[i].get(j).isUtilizada()) {

                        RunnableDFS.colorirResultado(grafo.getAdj()[i].get(j).getV1().getId(), grafo.getAdj()[i].get(j).getV2().getId(), validade);

                    }
                }

            }
        }

    }

    /**
     *
     * @param v1        vértice 1 da aresta a ser colorida
     * @param v2        vértice 2 da aresta a ser colorida
     * @param validade  true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa
     * @throws          java.lang.InterruptedException exception
     */
    public static void colorirResultado(int v1, int v2, boolean validade) throws InterruptedException {
        
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
        if (validade) {
            
            //verifico qual é a cor futura. Ser for verde claro, não precisa aplicar o gradient, se for cinza tenho que marcar que o sentido é contrário
            String cor = "";
            if (r == 152) {
                cor = "claro";
            } else if (r == 211) {
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

                //colorindo sem gradient
                if (cor.equals("claro")) {

                    linhas[0].setStroke(Color.rgb(r, g, b));
                    linhas[1].setStroke(Color.rgb(r, g, b));

                } else {
                    //invertendo o sentido
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

                    //criando a task para colorir a aresta. Ele é necessária pois se fizer o sleep aqui a aplicação trava, pois vai mandar a thread 
                    //do java parar e isso não pode ser feito
                    Task task = new Task<Void>() {
                        @Override
                        public Void call() throws InterruptedException {
                            linhas[1].setStrokeWidth(5);
                            linhas[0].setStrokeWidth(5);
                            double i = 0, j = 0;

                            while ((i <= 1) || (j <= 1)) {

                                Stop[] stops = new Stop[]{new Stop(j, Color.rgb(r, g, b)), new Stop(i, Color.rgb(r2, g2, b2))};
                                LinearGradient linearGradient = new LinearGradient(startX, stratY, endX, endY, false, CycleMethod.NO_CYCLE, stops);

                                linhas[0].setStroke(linearGradient);

                                Stop[] stops1 = new Stop[]{new Stop(j, Color.rgb(r, g, b)), new Stop(i, Color.rgb(r2, g2, b2))};
                                LinearGradient linearGradient1 = new LinearGradient(startX2, stratY2, endX2, endY2, false, CycleMethod.NO_CYCLE, stops1);

                                linhas[1].setStroke(linearGradient1);

                                if (i > 1) {
                                    j += 0.01;
                                } else {
                                    i += 0.01;

                                }

                                Thread.sleep(AlgoritmoDFS.velocidade/40);

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

        }
    }

    /**
     *
     * @param id        Id do vértice a ser colorido
     * @param r         componente de cor
     * @param g         componente de cor
     * @param b         componente de cor
     * @param validade  true se a Thread que implementa a runnableBFS estiver ativa e false para não ativa
     */
    public static void colorirVertice(int id, int r, int g, int b, boolean validade) {
        if (validade) {

            Circle circle = (Circle) componentesCircle.getChildren().get(id);
            circle.setFill(Color.rgb(r, g, b));
        }

    }
 
}
