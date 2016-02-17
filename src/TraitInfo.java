/**
 * Created by Tom on 2016-02-17.
 */
public class TraitInfo {

    private Sex sex;

    public TraitInfo(Sex sex){
        this.sex=sex;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public enum Sex{
        MALE,FEMALE;
    }
}
