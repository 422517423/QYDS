package net.dlyt.qyds.web.common;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractUrlBasedView;

/**
 * Implementation of View for attachment file.
 */
public class AttachmentView extends AbstractUrlBasedView {
  @Override
  protected void renderMergedOutputModel(Map<String, Object> model,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    // Set content type
    response.setContentType(this.getContentType());
    // Open
    if (model != null && !model.isEmpty()) {
      Iterator<String> keys = model.keySet().iterator();
      OutputStream o = null;
      try {
        while (keys.hasNext()) {
          Object value = model.get(keys.next());
          if (value instanceof byte[]) {
            byte[] attachment = (byte[])value;
            o = response.getOutputStream();
            o.write(attachment);
            o.close();
            break;
          }
        }
      } finally {
        if (o != null) {
            o.close();
        }
      }
    }
  }
}
