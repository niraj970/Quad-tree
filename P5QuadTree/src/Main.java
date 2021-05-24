
import java.io.*;
import java.util.Scanner;

public class Main {

public static void main(String[] args) throws IOException{
if(args.length < 3 ) {
throw new IllegalArgumentException("exception");
}
File inFile = new File(args[0]);
File outFile1 =  new File(args[1]);
File outFile2 = new File(args[2]);
FileWriter writer = new FileWriter(outFile1);
FileWriter writer2 = new FileWriter(outFile2);
Scanner reader = new Scanner(inFile);
Image sq = new Image();
sq.numrows = reader.nextInt();
sq.numcols = reader.nextInt();
sq.minval = reader.nextInt();
sq.maxval = reader.nextInt();

int squaresize = sq.computesquare(sq.numrows, sq.numcols);
       int[][] imgAry = new int[squaresize][squaresize];
sq.zero2DArray(imgAry);

writer2.write("zeroArray2D::\n");
for(int i =0; i < squaresize; i++) {
for(int j =0; j < squaresize; j++) {
writer2.write(""+imgAry[i][j]);
}writer2.write("\n");;
}

sq.loadImage(inFile, imgAry);

writer2.write("LoadImage::\n");
for(int i =0; i < squaresize; i++) {
for(int j =0; j < squaresize; j++) {
writer2.write(""+imgAry[i][j]);
}writer2.write("\n");
}


QTreeNode QTroot = new QTreeNode();
QuadTree Tree = new QuadTree();
QTroot = Tree.buildQuadTree(imgAry, 0, 0, squaresize);

writer.write("\nPreOrderTraversal::\n");
Tree.preorder(QTroot, writer);

writer.write("\nPostOrderTraversal::\n");
Tree.postorder(QTroot, writer);
reader.close();
writer.close();
writer2.close();




}}
///////////////////////////////
class Image{
int numrows;
int numcols;
int minval;
int maxval;
int squaresize ;
int[][] imgAry;

 public Image() {

 }

public void loadImage(File inFile, int[][] imgAry1) throws IOException{
Scanner reader = new Scanner(inFile);
reader.nextLine();
for(int i = 0; i < this.numrows; i++) {
for( int j = 0; j<this.numcols; j++) {
imgAry1[i][j] = reader.nextInt();
}
}
imgAry = imgAry1;
reader.close();
}

public void zero2DArray(int[][] imgAry1) {
imgAry = new int[squaresize][squaresize];
for (int i=0; i < squaresize; i++) {
for (int j=0; j < squaresize; j++) {
imgAry1[i][j] = 0;
}
}
imgAry = imgAry1;
}

public int computesquare(int numRows, int numCols) {
int square = Math.max(numRows, numCols);
int power2 = 2;
while(square > power2) {
power2 *= 2;
}
return power2;
}
}

/////////////////////////
class QTreeNode{
int color;
int upperRow;
int upperCol;
int size;
QTreeNode NWkid = null;
QTreeNode NEkid = null;
QTreeNode SWkid = null;
QTreeNode SEkid= null;

public QTreeNode(){

}

public QTreeNode(int color1, int upperRow1,int upperCol1, int size1, QTreeNode NWkid1,
QTreeNode NEkid1, QTreeNode SWkid1, QTreeNode  SEkid1) {
color = color1;
upperRow = upperRow1;
upperCol = upperCol1;
NWkid = NWkid1;
NEkid = NEkid1;
SWkid = SWkid1;
SEkid = SEkid1;
size = size1;

}

public void printNode(QTreeNode QNode, FileWriter outFile1){
String word = "";
if(QNode.NWkid == null) {
word = "(" + QNode.color + ", " + QNode.upperRow + ", "
+ QNode.upperCol + ", " + null + ", " + null
+", " + null + ", " + null + ")\n";
}else {
word = "(" + QNode.color + ", " + QNode.upperRow + ", " +  QNode.upperCol +
", " + QNode.NWkid.color + ", " + QNode.NEkid.color +
       ", " + QNode.SWkid.color + ", " + QNode.SEkid.color + ")\n";
}

try {
	outFile1.write(word);
	outFile1.flush();
} catch (IOException e) {
	e.printStackTrace();
}
}
}

///////////////////////////

class QuadTree{
QTreeNode QTRoot;

public QuadTree() {

}

public QTreeNode buildQuadTree(int[][] imgAry, int upperR, int upperC, int size) {
QTreeNode newQNode = new QTreeNode(-1, upperR, upperC, size, null, null, null, null);
     
   if (size == 1 ) {
  newQNode.color = imgAry[upperR][upperC];
   }else {
  int halfsize = size/2;
  newQNode.NWkid = buildQuadTree(imgAry, upperR, upperC, halfsize);
  newQNode.NEkid = buildQuadTree(imgAry, upperR, upperC+halfsize, halfsize);
  newQNode.SWkid = buildQuadTree(imgAry, upperR+halfsize, upperC, halfsize);
  newQNode.SEkid = buildQuadTree(imgAry, upperR+halfsize, upperC+halfsize, halfsize);

  int sumcolor = newQNode.NWkid.color + newQNode.NEkid.color + newQNode.SWkid.color + newQNode.SEkid.color;
  if (sumcolor == 0) {
  newQNode.color = 0;
  newQNode.NWkid = null;
  newQNode.NEkid = null;
  newQNode.SWkid = null;
  newQNode.SEkid = null;
  }else if(sumcolor == 4){
  newQNode.color = 1;
  newQNode.NWkid = null;
  newQNode.NEkid = null;
  newQNode.SWkid = null;
  newQNode.SEkid = null;  
  }else {
  newQNode.color = 5;
  }
 }
return newQNode;
}

public void preorder(QTreeNode Qt, FileWriter outFile1) throws IOException {
QTreeNode point = new QTreeNode();
if (isleaf(Qt)) {
point.printNode(Qt, outFile1);
}
else {
point.printNode(Qt, outFile1);
preorder(Qt.NWkid, outFile1);
preorder(Qt.NEkid, outFile1);
preorder(Qt.SWkid, outFile1);
preorder(Qt.SEkid, outFile1);
}
}

public void postorder(QTreeNode Qt, FileWriter outFile1) throws IOException{
QTreeNode point = new QTreeNode();
if(isleaf(Qt)) {
point.printNode(Qt, outFile1);
}
else {
postorder(Qt.NWkid, outFile1);
postorder(Qt.NEkid, outFile1);
postorder(Qt.SWkid, outFile1);
postorder(Qt.SEkid, outFile1);
point.printNode(Qt, outFile1);
}
}

public boolean isleaf(QTreeNode Qt) {
if ((Qt.NWkid == null) && (Qt.NEkid == null) && (Qt.SWkid == null) && (Qt.SEkid == null)) {
return true;
}
return false;
}
}