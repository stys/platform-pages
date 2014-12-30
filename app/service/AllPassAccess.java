package service;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.Page;
import com.stys.platform.pages.impl.utils.ResultUtils;
import play.libs.F;
import play.mvc.Content;

/**
 *
 */
public class AllPassAccess implements Service<Result<Content>, Page> {

    private ResultUtils<Content> results = new ResultUtils<>();

    @Override
    public Result<Content> get(String namespace, String key, F.Option<Long> revision) {
        if (namespace.equals("restricted")) {
            return results.Unauthorized(new Content() {
                @Override
                public String body() {
                    return "Unauthorized";
                }

                @Override
                public String contentType() {
                    return "text/plain";
                }
            });
        }
        if (namespace.equals("protected")) {
            return results.Forbidden(new Content() {
                @Override
                public String body() {
                    return "Forbidden";
                }

                @Override
                public String contentType() {
                    return "text/plain";
                }
            });
        }
        return results.Ok(null);
    }

    @Override
    public Result<Content> put(Page page, String namespace, String key, F.Option<Long> revision) {
        return results.Ok(null);
    }
}
