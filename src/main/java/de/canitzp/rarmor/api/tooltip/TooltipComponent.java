package de.canitzp.rarmor.api.tooltip;

import javax.management.AttributeList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class TooltipComponent {

    private List<List<Object>> objects = new ArrayList<>();
    private List<Object> currentObjects = new ArrayList<>();

    public TooltipComponent addText(String text){
        this.currentObjects.add(text);
        return this;
    }

    public TooltipComponent addRenderer(IComponentRender render){
        this.currentObjects.add(render);
        return this;
    }

    public TooltipComponent newLine(){
        this.objects.add(new ArrayList<>(this.currentObjects));
        this.currentObjects.clear();
        return this;
    }

    public List<List<Object>> endComponent(){
        this.objects.add(new ArrayList<>(this.currentObjects));
        return this.objects;
    }

}
