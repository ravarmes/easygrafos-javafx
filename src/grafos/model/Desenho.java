/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos.model;

import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * Classe utilizada para realizar o desenho do grafo.
 *
 * @author john_
 */
public class Desenho {

    /**
     *
     * @param grafoDesenho Elemento gerado depois da leitura do arquivo txt.
     * Possui as arestas e os vértices
     * @param anchorPaneGrafo AnchorPane onde o desenho do grafo será feito
     * @param componentesL Grupo com os Componentes line
     * @param componentesC Grupo com os Componentes Circle
     * @param componentesT Grupo com os Componentes text
     */
    public void desenharGrafo(Grafo grafoDesenho, AnchorPane anchorPaneGrafo, Group componentesL, Group componentesC, Group componentesT) {
        int contElementos = 0;
        Line line = new Line();
        int i = 0, j = 0, count = 0;

        for (i = 0; i < grafoDesenho.V(); i++) {
            count = grafoDesenho.getAdj()[i].size();
            for (j = 0; j < count; j++) {
                line.setStroke(Color.rgb(0, 0, 0));
                line.setStrokeWidth(3);

                line.setStartX(grafoDesenho.getAdj()[i].get(j).getV1().getX());
                line.setStartY(grafoDesenho.getAdj()[i].get(j).getV1().getY());
                line.setEndX(grafoDesenho.getAdj()[i].get(j).getV2().getX());
                line.setEndY(grafoDesenho.getAdj()[i].get(j).getV2().getY());
                line.setId(Integer.toString(grafoDesenho.getAdj()[i].get(j).getId()));

                componentesL.getChildren().add(line);
                contElementos++;
                line = new Line();
            }
        }

        contElementos = 0;
        for (i = 0; i < grafoDesenho.V(); i++) {
            Circle circle1 = new Circle();
            circle1.setCenterX(grafoDesenho.getListVertice().get(i).getX());
            circle1.setCenterY(grafoDesenho.getListVertice().get(i).getY());
            circle1.setRadius(15);
            circle1.setFill(Color.rgb(255, 255, 255));
            circle1.setStroke(Color.rgb(0, 0, 0));

            circle1.setStrokeWidth(3);
            circle1.setId(String.valueOf(grafoDesenho.getListVertice().get(i)));

            Text text1 = new Text(circle1.getCenterX(), circle1.getCenterY(), String.valueOf(grafoDesenho.getListVertice().get(i).getId()));
            centralizarText(text1, circle1);
            componentesC.getChildren().add(circle1);
            componentesT.getChildren().add(text1);
            contElementos++;
        }

        anchorPaneGrafo.getChildren().addAll(componentesL);
        anchorPaneGrafo.getChildren().addAll(componentesC);
        anchorPaneGrafo.getChildren().addAll(componentesT);
    }

    public void desenharGrafo(GrafoPonderado grafoDesenho, AnchorPane anchorPaneGrafo, Group componentesL, Group componentesC, Group componentesT) {
        int contElementos = 0;
        Line line = new Line();
        int i = 0, j = 0, count = 0;

        for (i = 0; i < grafoDesenho.V(); i++) {
            count = grafoDesenho.getAdj()[i].size();
            for (j = 0; j < count; j++) {
                line.setStroke(Color.rgb(0, 0, 0));
                line.setStrokeWidth(3);

                line.setStartX(grafoDesenho.getAdj()[i].get(j).getV1().getX());
                line.setStartY(grafoDesenho.getAdj()[i].get(j).getV1().getY());
                line.setEndX(grafoDesenho.getAdj()[i].get(j).getV2().getX());
                line.setEndY(grafoDesenho.getAdj()[i].get(j).getV2().getY());
                line.setId(Integer.toString(grafoDesenho.getAdj()[i].get(j).getId()));

                componentesL.getChildren().add(line);
                contElementos++;
                line = new Line();
            }
        }

        contElementos = 0;
        for (i = 0; i < grafoDesenho.V(); i++) {
            Circle circle1 = new Circle();
            circle1.setCenterX(grafoDesenho.getListVertice().get(i).getX());
            circle1.setCenterY(grafoDesenho.getListVertice().get(i).getY());
            circle1.setRadius(15);
            circle1.setFill(Color.rgb(255, 255, 255));
            circle1.setStroke(Color.rgb(0, 0, 0));

            circle1.setStrokeWidth(3);
            circle1.setId(String.valueOf(grafoDesenho.getListVertice().get(i)));

            Text text1 = new Text(circle1.getCenterX(), circle1.getCenterY(), String.valueOf(grafoDesenho.getListVertice().get(i).getId()));
            centralizarText(text1, circle1);
            componentesC.getChildren().add(circle1);
            componentesT.getChildren().add(text1);
            contElementos++;
        }

        anchorPaneGrafo.getChildren().addAll(componentesL);
        anchorPaneGrafo.getChildren().addAll(componentesC);
        anchorPaneGrafo.getChildren().addAll(componentesT);
    }

    /**
     *
     * @param text Elemento do tipo Text com o id do Circle
     * @param circle Circle que receberá o Text
     */
    private void centralizarText(Text text, Circle circle) {
        double W = text.getBoundsInLocal().getWidth();
        double H = text.getBoundsInLocal().getHeight();
        text.relocate(circle.getCenterX() - W / 2, circle.getCenterY() - H / 2);
    }

}
