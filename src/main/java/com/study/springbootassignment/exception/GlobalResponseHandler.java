package com.study.springbootassignment.exception;


import com.study.springbootassignment.exception.model.ErrorResponse;
import com.study.springbootassignment.exception.model.Meta;
import com.study.springbootassignment.exception.model.PagedResponse;
import com.study.springbootassignment.exception.model.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Instant;
import java.util.List;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true; // apply to all controllers
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  org.springframework.http.server.ServerHttpRequest serverHttpRequest,
                                  org.springframework.http.server.ServerHttpResponse serverHttpResponse) {

        // Already wrapped
        if (body instanceof ErrorResponse) return body;
        int httpStatus = response.getStatus();
        String traceId = (String) request.getAttribute("traceId");
        boolean isError = httpStatus >= 400;
        int code = isError ? -1 : 0;
        String message = isError ? "Error" : "Success";
        String timeStam = Instant.now().toString();
        if (body instanceof Page<?> page) {
            List<?> content = page.getContent();
            Meta meta = new Meta(
                    page.getNumber(),
                    page.getSize(),
                    (int) page.getTotalElements(),
                    page.hasNext(),
                    page.hasPrevious()
            );
            PagedResponse<Object> response = new PagedResponse<>();
            response.setCode(0);
            response.setStatus(200);
            response.setData((List<Object>) content);
            response.setMessage("Success");
            response.setTraceId(traceId);
            response.setPath(request.getRequestURI());
            response.setTimeStam(timeStam);
            response.setMeta(meta);
            return response;
        }

        return new Response<>(
                code,
                httpStatus,
                body,
                message,
                traceId,
                request.getRequestURI(),
                timeStam
        );
    }
}
