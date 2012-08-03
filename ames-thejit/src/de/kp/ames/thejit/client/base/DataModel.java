package de.kp.ames.thejit.client.base;

public class DataModel {

	enum TreeType {
        HYPERTREE, RGRAPH
    }

    private TreeType treeType;

    public TreeType getTreeType() {
        return treeType;
    }

    public void setTreeType(TreeType treeType) {
        this.treeType = treeType;
    }

}
