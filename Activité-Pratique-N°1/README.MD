<h2>Inversion de contrôle et Injection des dépendances</h2>
<p>Partie 1 :</p>
<p>
1. Créer l'interface IDao avec une méthode getDate<br> 
2. Créer une implémentation de cette interface<br>  
3. Créer l'interface IMetier avec une méthode calcul<br> 
4. Créer une implémentation de cette interface en utilisant le couplage faible<br>
5. Faire l'injection des dépendances :<br>
  &nbsp;&nbsp;a. Par instanciation statique:
</p>
<img src="captures/static.png"></img>
<img src="captures/resultStatic.png"></img>
<p>
&nbsp;&nbsp;b. Par instanciation dynamique:
</p>
<img src="captures/dynamique.png"></img>
<img src="captures/resultDynamique2.png"></img>
<p>
&nbsp;&nbsp;c. En utilisant le Framework Spring<br>
       &nbsp;&nbsp;&nbsp;&nbsp;- Version XML<br>
       &nbsp;&nbsp;&nbsp;&nbsp;- Version annotations
</p>
<img src="captures/annotation1.png"></img>
<img src="captures/annotation2.png"></img>
<h3>Diagramme de Classes</h3>
<img src="captures/diagrame.png"></img>
