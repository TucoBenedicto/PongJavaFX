/*******************************************************************************
 * Copyright (C) 2018 ROMAINPC_LECHAT
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.example.Pong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    private int dx; //deplacement de la ball sur X
    private int dy; //deplacement de la ball sur Y
    private int score; //Score

    public void start(Stage primaryStage) {

        /*
        L'instruction try...catch regroupe des instructions à exécuter et définit une réponse si l'une de ces instructions provoque une exception.
         */
        try {
            /*
            Group est un conteneur simple (il en existe d'autre ...), il est un composant qui n'applique pas la mise en page (Layout) pour ses éléments enfants. Tous les éléments d'enfants sont été placés 0,0. Le but de Groupe est de rassembler des contrôles et d'effectuer une certaine tâche.
            par exemple un conteneur pour la fenetre de jeu et un autre pour les scores.
             */
            Group root = new Group();
            //Scene = fenetre de l'application ,sans le bandeau titre/bandeau de la fenetre.
            //scene permet de rentrer la taille de la fenetre.
            Scene scene = new Scene(root, 1000, 600);
            //La methode setFill permet de remplir un element (ici la scene)par une couleur , degradé ...
            scene.setFill(new RadialGradient(0, 0, 500, 300, 500, false, CycleMethod.NO_CYCLE, new Stop(0, Color.grayRgb(60)), new Stop(1, Color.BLACK))); //Degradé radial

            //objets:
            Circle balle = new Circle(500, 300, 25);//Creation de la balle (500 position en x,300 en y, 25 : rayon
            balle.setFill(Color.DARKOLIVEGREEN);//on ajoute la couleur a la balle

            Rectangle joueur = new Rectangle(890, 250, 30, 100);//Creation de la palettes du joueur (x:890, y:250, width:30, height:100)
            joueur.setArcHeight(5);//definit un arrondi sur les angle du rectangle (cote vertical)
            joueur.setArcWidth(5);//definit un arrondi sur les angle du rectangle (cote horizontal)
            joueur.setFill(Color.CORNFLOWERBLUE);

            Rectangle IA = new Rectangle(80, 250, 30, 100); //Creation de la palette pour l'AI (x:80, y:250, width:30, height:100)
            IA.setArcHeight(5);
            IA.setArcWidth(5);
            IA.setFill(Color.CORNFLOWERBLUE);

            //Affichage du  "Perdu"
            Text perdu = new Text("PERDU !");//Ajout d'un text
            perdu.setFont(Font.font("Verdana", 70));//on definit sa police de caractere
            perdu.setFill(Color.RED);//definit la couleur
            //perdu.setTextOrigin(VPos.CENTER);
            perdu.setLayoutX(400);//definit la position du text sur l'axe des X
            perdu.setLayoutY(300);//definit la position du text sur l'axe des Y
            perdu.setVisible(false);//par defaut il est invisible (false) pour eviter que le text ne s'affiche pas des le debut de la partie

            //Rassemblement de tous les elements
            //getChildren returns an observable "list" (joueur,IA,ball, perdu) of nodes. As any list,
            // you can get any of its items by their index. get(0) will return the first item in the list , exemple : Node nodeOut = root.getChildren().get(0);
            root.getChildren().addAll(joueur, IA, balle, perdu);//on ajoute/rassemble tous les elements(addAll) creer (joueur,IA,balle, perdu) que l'on integre a la scene (root)

            //Direction de la ball
            dx = -5; // incrementation de -5 sur l'axe X pour que la balle aille d'anord vers l'IA (+5 elle part vers le joueur)
            dy = 5;  // +5 pour que la balle descecende (-5 elle monte)
            score = 0;

            //souris:
            //Pour pouvoir commander le joueur à la sourie
            scene.setOnMouseMoved(e -> {
                joueur.setY(e.getSceneY() - 50);
            });

            //Clavier:
            //Pour pouvoir commander le joueur au clavier
            scene.setOnKeyPressed(e->{
                if (e.getCode().equals(KeyCode.UP) && joueur.getY()>0){ //"joueur.getY()>0" : uniquement si la position en Y est >0 pour ne pas sortir de la fenetre
                    joueur.setY(joueur.getY()-10);//Chaque appui de la touche genere un deplacement de 10px vers le haut
                }
                if (e.getCode().equals(KeyCode.DOWN)&& joueur.getY()<500){
                    joueur.setY(joueur.getY()+10);
                }
            });


            // boucle de jeu ,Qui va creer l'animation
            /*
            Pour créer une ligne temporelle, il faut utiliser la classe "Timeline" ainsi que les classes associées.
            Une ligne de temps consiste en une séquence d'étapes ou d'animations-clés appelées "keyframes" placées dans une ligne temporelle.
            Ces étapes permettent d'indiquer les changements dans l'état des propriétés qui servent à animer un nœud graphique.
            Une étape-clé est modélisée par la classe javafx.animation.KeyFrame.
             */
            Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {//la vitesse de l'animation est de 10mS/image (0.01 seconde)
                public void handle(ActionEvent arg) {
                    //Test:
                    //System.out.println("ok");

                    //deplacement de la balle:
                    //getCenterX : recuper la coordonnée X de la ball
                    //setCenterX : definit une nouvelle coordonnée X , grace à +dx
                    balle.setCenterX(balle.getCenterX() + dx);
                    balle.setCenterY(balle.getCenterY() + dy);

                    //rebond au sommet de la fenetre:
                    if (balle.getCenterY() <= 25) { //la balle rencontre le sommet a 25px
                        dy = 5; //Alors la balle descend de 5px
                    }
                    //rebond au bas de la fenetre:
                    if (balle.getCenterY() >= 575) { //la balle rencontre le sol a 575px
                        dy = -5; //Alors la balle monte de 5px
                    }
                    //rebond à droite sur la raquette du "joueur":
                    if (balle.getCenterX() >= 865) { //Si la balle arrive à la bordure X du joueur
                        // si la balle rencontre la raquette en Y du joueur
                        // "joueur.getY()" : point haut de la raquette & "joueur.getY() + 100" : point bas de la rquette
                        if (balle.getCenterY() >= joueur.getY() && balle.getCenterY() <= joueur.getY() + 100) { //+ 100 : hauteur de la raquette
                            dx = -5; //au rebond on la renvoit dans la direction opposée
                        }
                    }
                    //rebond à gauche sur la rquette de L'IA:
                    if (balle.getCenterX() <= 110) {
                        if (balle.getCenterY() >= IA.getY() && balle.getCenterY() <= IA.getY() + 100) {
                            dx = 5; //au rebond on la renvoit dans la direction opposée
                            score++; //a chaque echange on augment le score +1
                            primaryStage.setTitle("Pong       SCORE : " + score); //le score est affiché dans la barre de titre
                        }
                    }

                    //deplacement IA:
                    //on s'assure que c'est le centre de la raquette qui va taper la balle
                    // !!! a la vitesse de 5 l'IA est imbattable , il faut aller vers une value < 5
                    //"IA.getY() + 50" : centre de la raquette
                    if (IA.getY() + 50 < balle.getCenterY()) { //Si les coordonnées de la balle sont en haut du milleu de la raquette
                        //IA.setY(IA.getY() + 5); //on deplacer l'IA vers le bas (+5px)// trop difficiel
                        IA.setY(IA.getY() + 3);
                    }
                    if (IA.getY() + 50 > balle.getCenterY()) { //Si les coordonnées de la balle sont sous le milleu de la raquette
                        //IA.setY(IA.getY() - 5); ////on deplacer l'IA vers le haut (-5px)
                        IA.setY(IA.getY() - 3);
                    }

                    // Defaite
                    //Si la balle franchie x:890 , cela indique que l'on a perdu car la balle a depassée la raquette du joueur
                    if (balle.getCenterX() >= 890) {
                        dx = 0; //on arrete le depalcement de la ball
                        dy = 0; //on arrete le depalcement de la ball
                        balle.setCenterX(920); // et on la fige a 920px en X
                        perdu.setVisible(true); //On affiche le texte "PERDU"
                    }
                }

            }));
            loop.setCycleCount(Timeline.INDEFINITE); // on definit la durée de l'animation , ici infini
            loop.play(); // on lance la timeline

            //primaryStage (stage) : The JavaFX Stage class is the top level JavaFX container , le second est "scene"
            primaryStage.setScene(scene); //on configure le stage (primaryStage) , stage : fenetre + barre de titre
            primaryStage.setResizable(false);//le stage ne peut etre redimensionnée
            primaryStage.setTitle("Pong       SCORE : 0");//on affiche du texte dans la barre de titre
            primaryStage.show(); //on affiche le stage

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
