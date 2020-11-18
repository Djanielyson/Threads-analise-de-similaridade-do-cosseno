/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package removeme;

import image.mapping.Image;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rnsg
 */
public class Testador {

    public static void main(String[] args) throws IOException {

        //String filename01 = "images/exem/fotoalta.tiff";
        String filename01 = "images/exem/comparable.tif";
        Image[] vetor = new Image[2];
        String index = "images\\exem";

        Files.walk(Paths.get(index)).forEach((arquivo) -> {
            if (!arquivo.toString().contains(".")) {
                return;
            }
            String filename02 = arquivo.toString();

            Thread td0 = new Thread(() -> {
                if(!arquivo.toString().contains(".jpg")){
                    return;
                }
                
                //tarefa0 - converte a foto de jpg para tif
                String input = "images/exem/fotoalta.jpg";
                ImageConverter.convertTiff(input);

            });

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
                    vetor[0] = new Image(Image.componentExtract(filename02), 255);//subtarefa1
                    vetor[1] = new Image(Image.componentExtract(filename01), 255);//subtarefa1

                } catch (IOException e) {
                }

            });
            td1.start();

            Thread td2 = new Thread(() -> {
                try {
                    td1.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Testador.class.getName()).log(Level.SEVERE, null, ex);
                }

                //tarefa2 - pega os componentes das duas imagens e compara para verificar a similaridade usando SimCosseno
                double similarityCosine = vetor[1].compareCosineSimilarity(vetor[0]);
                System.out.printf("\nSIMILARIDADE DO COSSENO: %.2f%%\n", (similarityCosine * 100));
                System.out.println("Arquivo: " + arquivo.toString());

            });
            td2.start();

        });

    }

}
