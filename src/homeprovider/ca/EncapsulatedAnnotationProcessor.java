package homeprovider.ca;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@SupportedAnnotationTypes(value ={"homeprovider.ca.Encapsulated"})
public class EncapsulatedAnnotationProcessor extends AbstractProcessor {
	private Messager messager;


	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);

		messager = processingEnv.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		List<String> fieldNames = new ArrayList<>();
		int checkCount = 0;

			for (Element aE : roundEnv.getElementsAnnotatedWith(Encapsulated.class)) {

				for (Element eE: aE.getEnclosedElements()) {

					if (eE.getKind() == javax.lang.model.element.ElementKind.FIELD) {

						if (! eE.getModifiers().contains(Modifier.PRIVATE)) {
							messager.printMessage(Diagnostic.Kind.ERROR, " Not encapsulated class", aE);
							break;
						}
						else fieldNames.add(eE.getSimpleName().toString());
					}

					for (String s: fieldNames) {

						if (eE.getSimpleName().toString()
								.equals("set"+ s.substring(0,1).toUpperCase() + s.substring(1)) ||
							eE.getSimpleName().toString()
								.equals("get"+s.substring(0,1).toUpperCase() + s.substring(1))) {

							checkCount++;
						}
					}
				}
				if (checkCount != fieldNames.size() * 2)
					messager.printMessage(Diagnostic.Kind.ERROR," Not encapsulated class", aE);
			}
		return false;
	}
}
