/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-20
 * @desc      : A comment-related service interface. that define business logic for comment.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 */

package backendnovice.projectbookshop.board.comment.service;

import backendnovice.projectbookshop.board.comment.dto.CommentDTO;

public interface CommentService {
    public void write(CommentDTO commentDTO);
}
