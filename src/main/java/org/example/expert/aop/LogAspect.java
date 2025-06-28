package org.example.expert.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.log.LogService;
import org.example.expert.log.ManagerLogAop;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final LogService logService;

    @Pointcut("@annotation(managerLogAop)")
    private void managerLogAopPointCut(ManagerLogAop managerLogAop) {}

    @Around(value = "managerLogAopPointCut(managerLogAop)", argNames = "joinPoint,managerLogAop")
    public Object saveLog(ProceedingJoinPoint joinPoint, ManagerLogAop managerLogAop) throws Throwable {

        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = authUser.getId();
        Long todoId = (Long) joinPoint.getArgs()[1];

        try{
            Object aopResult = joinPoint.proceed();
            logService.saveLog(userId, todoId, true, managerLogAop.action());
            return aopResult;
        }catch (Exception exception) {
            logService.saveLog(userId, todoId, false, managerLogAop.action());
            throw exception;
        }
    }
}
