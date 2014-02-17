package implementation;

/**
 * Representation d'un arbre binaire.
 * 
 * @author Clément
 * 
 */
public class BinaryTree {
	private int[] tab;
	private int sizeMax;
	private int currentSize;

	public BinaryTree(int sizeMax) {
		this.currentSize = 0;
		this.sizeMax = sizeMax;
		this.tab = new int[this.sizeMax];
	}

	public void add(int valeur) {
		int currentPosition = currentSize;
		this.tab[currentSize] = valeur;
		while (currentPosition != 0 && this.tab[this.getIndexParent(currentPosition)] > valeur) {
			permuter(this.getIndexParent(currentPosition), currentPosition);
			currentPosition = this.getIndexParent(currentPosition);
		}
		this.currentSize++;
	}

	public void supprimerTete() {
		this.currentSize--;
		this.permuter(0, currentSize);
		int currentPosition = 0;

		while (hasChild(currentPosition)) {
			if (this.tab[2 * currentPosition + 1] <= this.tab[2 * currentPosition + 2]) {
				// gauche
				this.permuter(currentPosition, 2 * currentPosition + 1);
				currentPosition = 2 * currentPosition + 1;
			} else {
				// droite
				this.permuter(currentPosition, 2 * currentPosition + 2);
				currentPosition = 2 * currentPosition + 2;
			}
		}
	}

	/**
	 * @param currentPosition
	 * @return
	 */
	private boolean hasChild(int currentPosition) {
		return hasLeftChild(currentPosition) && this.tab[currentPosition] > this.tab[2 * currentPosition + 1] || hasRightChild(currentPosition)
				&& this.tab[currentPosition] > this.tab[2 * currentPosition + 2];
	}

	/**
	 * @param indexFils
	 * @return
	 */
	private int getIndexParent(int indexFils) {
		int indiceParent = 0;
		if (indexFils % 2 == 0) {
			indiceParent = (indexFils - 2) / 2;
		} else {
			indiceParent = (indexFils - 1) / 2;
		}
		return indiceParent;
	}

	private void permuter(int indiceParent, int indiceFils) {
		int tmpFils = this.tab[indiceFils];
		this.tab[indiceFils] = this.tab[indiceParent];
		this.tab[indiceParent] = tmpFils;
	}

	private boolean hasLeftChild(int index) {
		return index < currentSize && index < 2 * currentSize + 1;
	}

	private boolean hasRightChild(int index) {
		return index < currentSize && index < 2 * currentSize + 2;
	}

	public String toString() {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < this.currentSize; i++) {
			// racine
			res.append(tab[i] + "|");
			// res.append("\t");
			// fils gauche
			// res.append(tree[2 * i + 1]);
			// fils droit
			// res.append(tree[2 * i + 2]);
		}
		return res.toString();
	}

	public static void main(String[] args) {
		BinaryTree tree = new BinaryTree(7);
		tree.add(2);
		tree.add(4);
		tree.add(6);
		tree.add(5);
		tree.add(12);
		tree.add(10);
		tree.add(1);
		System.out.println(tree.toString());
		tree.supprimerTete();
		System.out.println("delete : " + tree.toString());
		tree.supprimerTete();
		System.out.println("delete : " + tree.toString());
	}

}
