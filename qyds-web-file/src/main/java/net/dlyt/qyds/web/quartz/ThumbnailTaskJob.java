package net.dlyt.qyds.web.quartz;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ThumbnailTaskJob extends QuartzJobBean{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ThumbnailTaskJob.class);

	private static int[] resizeArray = {120, 480, 800, 1200};

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		JobDataMap paramMap = context.getJobDetail().getJobDataMap();

		String shortUrl = paramMap.getString("short_image_url");

		String srcImg = null;
		String destImg = null;

		String basePath = paramMap.getString("storagePath");

		try {
			for (int w : resizeArray) {
				srcImg = basePath.concat("/orignal");
				srcImg = srcImg.concat(shortUrl);
				destImg = basePath.concat("/thumb" + w);
				destImg = destImg.concat(shortUrl);
				mkdirs(destImg);
				File file = new File(destImg);
				if(file.exists()){
					continue;
				}

				logger.debug("src=" + srcImg);
//				System.out.println("src=" + srcImg);
				scaleResize(srcImg, destImg, w, w, true);
//				System.out.println("dest=" + destImg);
				logger.debug("dest=" + destImg);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * 等比缩放图片（如果width为空，则按height缩放; 如果height为空，则按width缩放）
	 *
	 * @param srcImg 源图片路径
	 * @param destImg 目标图片路径
	 * @param width 缩放后的宽度
	 * @param height 缩放后的高度
	 * @param isResize 是否以缩放方式，而非缩略图方式
	 * @throws Exception
	 */
	public void scaleResize(String srcImg, String destImg,
							Integer width, Integer height, boolean isResize) throws Exception {
		IMOperation op = new IMOperation();
		op.addImage(srcImg);
		if(isResize){
			op.resize(width, height);
		}else {
			op.sample(width, height);
		}
		op.addImage(destImg);
		ConvertCmd cmd = new ConvertCmd(true);
		cmd.run(op);
	}


	public boolean mkdirs(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return file.getParentFile().mkdirs();
	}
}
