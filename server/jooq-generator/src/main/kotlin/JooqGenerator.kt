import org.jooq.codegen.DefaultGeneratorStrategy
import org.jooq.codegen.GeneratorStrategy
import org.jooq.codegen.JavaWriter
import org.jooq.codegen.KotlinGenerator
import org.jooq.meta.Definition

@Suppress("UNUSED") // used by jooq in build.gradle.kts
class JooqGenerator : KotlinGenerator() {

    override fun printClassAnnotations(
        out: JavaWriter,
        definition: Definition?,
        mode: GeneratorStrategy.Mode
    ) {
        super.printClassAnnotations(out, definition, mode)
        if (mode == GeneratorStrategy.Mode.POJO) {
            out.println("@kotlinx.serialization.Serializable")
        }
    }
}

@Suppress("UNUSED") // used by jooq in build.gradle.kts
class JooqStrategy : DefaultGeneratorStrategy() {

    override fun getJavaClassName(definition: Definition?, mode: GeneratorStrategy.Mode?): String {
        if (mode == GeneratorStrategy.Mode.POJO) {
            return "${super.getJavaClassName(definition, mode)}DTO"
        }
        return super.getJavaClassName(definition, mode)
    }

    override fun getJavaPackageName(definition: Definition?, mode: GeneratorStrategy.Mode?): String {
        return super.getJavaPackageName(definition, mode)
            .replace("tables.pojos", "common.models")
    }
}
