package diophantine_equations;

public class DKey implements Comparable<DKey>{
    private Integer keyX=0;
    private Integer keyY=0;
    
    public DKey(int key1, int key2) {
       
        this.keyX = key1;
        this.keyY = key2;
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof DKey))
            return false;
       DKey k = (DKey) o;
        return k.keyX.equals(keyX) && k.keyY.equals(keyY);
    }
    
    public int compareTo(DKey k) {
        int lastCmp = keyX.compareTo(k.keyX);
        return (lastCmp != 0 ? lastCmp : keyY.compareTo(k.keyY));
    }
    
    public int hashCode() {
        return 31*keyX.hashCode() + keyY.hashCode();
    }
   public long getKeyX() {
       return keyX;
   }
   public void setKeyX(int keyX) {
       this.keyX = keyX;
   }
   public long getKeyY() {
       return keyY;
   }
   public void setKeyY(int keyY) {
       this.keyY = keyY;
   }
}
