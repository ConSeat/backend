package site.concertseat.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import site.concertseat.domain.admin.dto.req.ChangeReviewStatusReq;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class AdminLogAspect {
    private static final Logger adminLogger = LoggerFactory.getLogger("adminLogger");

    @Pointcut("execution(@org.springframework.web.bind.annotation.PatchMapping * site.concertseat.domain.admin..*(..))")
    public void patchAdminApi() {}

    @AfterReturning(pointcut = "patchAdminApi()")
    public void logAdminPatchRequest(JoinPoint joinPoint) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String uri = request.getRequestURI();
        String method = request.getMethod();

        Object[] args = joinPoint.getArgs();
        Optional<Object> maybeBody = Arrays.stream(args)
                .filter(arg -> arg instanceof ChangeReviewStatusReq)
                .findFirst();;

        maybeBody.ifPresent(body -> {
            try {
                String nickname = null;
                if (body instanceof ChangeReviewStatusReq reqDto) {
                    nickname = reqDto.getInspectorName();
                }

                adminLogger.info("[PATCH ADMIN] {} {} with name={}",
                        method, uri, nickname);

            } catch (Exception e) {
                adminLogger.warn("Failed to parse request body for admin log: {}", e.getMessage());
            }
        });
    }
}

