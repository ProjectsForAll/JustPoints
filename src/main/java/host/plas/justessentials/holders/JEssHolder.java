package host.plas.justessentials.holders;

import host.plas.bou.compat.ApiHolder;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter @Setter
public abstract class JEssHolder<P> extends ApiHolder<P> {
    public JEssHolder(String identifier, Function<Void, P> getter) {
        super(identifier, getter);
    }
}
