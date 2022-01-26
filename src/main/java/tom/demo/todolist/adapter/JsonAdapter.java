package tom.demo.todolist.adapter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface JsonAdapter<D, J> {

	default Collection<J> convertDomainToJSON(List<D> domainObjects) {
		return domainObjects.stream()
				.map(it -> convertDomainToJSON(it))
				.collect(Collectors.toList());
	}

	J convertDomainToJSON(D domainObject);

}
