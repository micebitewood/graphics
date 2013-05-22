public class Antialiasing
{
	static int[] rgb = new int[3];
	static int[] rgbBuffer = new int[3];
	static int[] avg = new int[3];
	public static void antialiasing(MISApplet mis)
	{
		for(int y = 1; y < mis.H - 3; y += 1)
		{
			for(int i = 0; i < 3; i++)
			{
				rgb[i] = mis.unpack(mis.pix[y * mis.W], i);
				rgbBuffer[i] = rgb[i];
			}
			for(int x = 1; x < mis.W - 3; x += 1)
			{
				int ind = y * mis.W + x;
				int dist = 0;
				for(int i = 0; i < 3; i++)
				{
					rgb[i] = mis.unpack(mis.pix[ind], i);
					rgbBuffer[i] = mis.unpack(mis.pix[ind - 1], i);
					dist += Math.abs(rgb[i] - rgbBuffer[i]);
					rgbBuffer[i] = mis.unpack(mis.pix[ind - mis.W], i);
					dist += Math.abs(rgb[i] - rgbBuffer[i]);
					rgbBuffer[i] = mis.unpack(mis.pix[ind + 1], i);
					dist += Math.abs(rgb[i] - rgbBuffer[i]);
					rgbBuffer[i] = mis.unpack(mis.pix[ind + mis.W], i);
					dist += Math.abs(rgb[i] - rgbBuffer[i]);
				}
				if(x > 0 && dist >= 50)
				{
					
					for(int i = 0; i < 3; i++)
					{
						avg[i] = 0;
						/*
						avg[i] += mis.unpack(mis.pix[ind - 2 * mis.W], i);
						
						avg[i] += mis.unpack(mis.pix[ind - mis.W - 1], i);
						avg[i] += mis.unpack(mis.pix[ind - mis.W], i);
						avg[i] += mis.unpack(mis.pix[ind - mis.W + 1], i);

						avg[i] += mis.unpack(mis.pix[ind - 2], i);
						avg[i] += mis.unpack(mis.pix[ind - 1], i);
						*/
						
						avg[i] += rgb[i];
						avg[i] += mis.unpack(mis.pix[ind + 1], i);
						avg[i] += mis.unpack(mis.pix[ind + 2], i);
						avg[i] += mis.unpack(mis.pix[ind + 3], i);

						//avg[i] += mis.unpack(mis.pix[ind + mis.W - 1], i);
						avg[i] += mis.unpack(mis.pix[ind + mis.W], i);
						avg[i] += mis.unpack(mis.pix[ind + mis.W + 1], i);
						avg[i] += mis.unpack(mis.pix[ind + mis.W + 2], i);
						avg[i] += mis.unpack(mis.pix[ind + mis.W + 3], i);
						
						avg[i] += mis.unpack(mis.pix[ind + 2 * mis.W], i);
						avg[i] += mis.unpack(mis.pix[ind + 2 * mis.W + 1], i);
						avg[i] += mis.unpack(mis.pix[ind + 2 * mis.W + 2], i);
						avg[i] += mis.unpack(mis.pix[ind + 2 * mis.W + 3], i);

						avg[i] += mis.unpack(mis.pix[ind + 3 * mis.W], i);
						avg[i] += mis.unpack(mis.pix[ind + 3 * mis.W + 1], i);
						avg[i] += mis.unpack(mis.pix[ind + 3 * mis.W + 2], i);
						avg[i] += mis.unpack(mis.pix[ind + 3 * mis.W + 3], i);

						avg[i] /= 16;
					}
					
					mis.pix[ind] = mis.pack(avg[0], avg[1], avg[2]);
					
					
					//mis.pix[ind - mis.W - 1] = mis.pix[ind];
					//mis.pix[ind - mis.W] = mis.pix[ind];
					//mis.pix[ind - mis.W + 1] = mis.pix[ind];
					
					//mis.pix[ind - 1] = mis.pix[ind];
					//mis.pix[ind + 1] = mis.pix[ind];

					//mis.pix[ind + mis.W - 1] = mis.pix[ind];
					//mis.pix[ind + mis.W] = mis.pix[ind];
					//mis.pix[ind + mis.W + 1] = mis.pix[ind];

				}
			}
		}
	}
}
