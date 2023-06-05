package im.fitdiary.swaggeragent.controller;

import im.fitdiary.swaggeragent.logger.Logger;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.MemberAttributeExtension;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.JavaModule;

import java.lang.annotation.Annotation;
import java.security.ProtectionDomain;

public class ControllerTransformer implements AgentBuilder.Transformer {

    private final Logger logger = new Logger(getClass());

    @Override
    public Builder<?> transform(
            Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            ProtectionDomain protectionDomain
    ) {
        TypePool typePool = TypePool.Default.of(classLoader);

        for (MethodDescription.InDefinedShape method : typeDescription.getDeclaredMethods()) {
            if (!method.isConstructor()) {
                Parameters parameters = new ParameterBuilder(typePool, method).build();
                ApiResponses apiResponses = new ApiResponseBuilder(typePool, method, classLoader).build();
                SecurityRequirements securityRequirements =
                        new SecurityRequirementBuilder(typePool, method).build();
                builder = addAnnotations(builder, method, parameters, apiResponses, securityRequirements);
            }
        }

        logger.success(typeDescription);
        return builder;
    }

    private Builder<?> addAnnotations(
            Builder<?> builder,
            MethodDescription.InDefinedShape method,
            Annotation... annotations
    ) {
        MemberAttributeExtension.ForMethod forMethod = new MemberAttributeExtension.ForMethod();
        for (Annotation annotation : annotations) {
            forMethod = forMethod.annotateMethod(annotation);
        }
        return builder.visit(forMethod.on(ElementMatchers.named(method.getName())));
    }

    // 바이트코드 확인용
//    private void saveFile(Builder<?> builder, TypeDescription typeDescription) {
//        try {
//            String path = System.getProperty("java.class.path").split(":")[0];
//            builder.name(typeDescription.getName() + "Sample")
//                    .make().saveIn(new File(path));
//            System.out.println("저장 성공!");
//        } catch (Exception e) {
//            System.out.println("저장 실패!");
//            System.out.println(e.toString());
//        }
//    }

    // 기존 중복된 어노테이션 제거
//    private Builder<?> removeduplicatedAnnotations(
//            Builder<?> builder,
//            MethodDescription.InDefinedShape method,
//            Set<TypeDescription> duplicatedAnnotations
//    ) {
//        return builder
//                .visit(new AsmVisitorWrapper.ForDeclaredMethods().method(
//                        ElementMatchers.named(method.getName()),
//                        (instrumentedType,
//                         instrumentedMethod,
//                         methodVisitor,
//                         implementationContext,
//                         typePool,
//                         writerFlags,
//                         readerFlags
//                        ) -> new MethodVisitor(OpenedClassReader.ASM_API, methodVisitor) {
//                            @Override
//                            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
//                                for (TypeDescription duplicatedAnnotation : duplicatedAnnotations) {
//                                    if (duplicatedAnnotation.getDescriptor().equals(descriptor)) {
//                                        return null;
//                                    }
//                                }
//                                return super.visitAnnotation(descriptor, visible);
//                            }
//                        }
//                ));
//    }
}
