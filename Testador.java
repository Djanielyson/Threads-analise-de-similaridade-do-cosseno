/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package removeme;

import image.mapping.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rnsg
 */
public class Testador {

    public static void main(String[] args) {

        String filename01 = "images/exem/fotoalta.tiff";
        String filename02 = "images/exem/comparable.tif";

        //Criando a primeira thread, utilizando o lambda para simplificar a utilização da uma interface funcional 
        Thread td0 = new Thread(() -> {
            //tarefa0 - converte a foto de jpg para tif
            String input = "images/exem/fotoalta.jpg";
            ImageConverter.convertTiff(input);
        });

        //Criando um Array para poder utilizar os valores de forma que o vetor é um final
        Image[] vetor = new Image[2];

        td0.start();

        //Criando a segunda thread, utilizando o lambda para simplificar a utilização da uma interface funcional
        Thread td1 = new Thread(() -> {
            try {
                //Utilizando join para fazer com que a thread2 aguarde o final da primeira
                td0.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Testador.class.getName()).log(Level.SEVERE, null, ex);
            }
            //tarefa1 - pega a foto tif e extrai os compenentes
            try {
                vetor[0] = new Image(Image.componentExtract(filename01), 255);//subtarefa1
                vetor[1] = new Image(Image.componentExtract(filename02), 255);//subtarefa1

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        td1.start();

        Thread td2 = new Thread(() -> {
            try {
                td1.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Testador.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Teste1");

            //tarefa2 - pega os componentes das duas imagens e compara para verificar a similaridade usando SimCosseno
            double similarityCosine = vetor[0].compareCosineSimilarity(vetor[1]);
            System.out.printf("\nSIMILARIDADE DO COSSENO: %.2f%%\n", (similarityCosine * 100));

        });

        td2.start();

        Thread td3 = new Thread(() -> {

            try {
                td1.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Testador.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Teste2");

            //tarefa3 - pega os componentes das duas imagens e compara para verificar a similaridade usando euclid dist
            double euclidean = vetor[0].compareEuclideanDistance(vetor[1]);
            System.out.printf("\nDistância Euclidiana:  %.2f\n", euclidean);

        });

        td3.start();

        Thread td4 = new Thread(() -> {

            try {
                td1.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Testador.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Teste 3");

            //tarefa4 - exibe a distribuiçao de pixels RGB em forma de histograma na imagem1
            TesteHistogram.printHistogramByMatrix(vetor[0].getHistogram()); //printHistogramFromFile("images/converter/teste1.tiff");
        });

        td4.start();

    }

}