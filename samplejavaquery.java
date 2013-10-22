package simpanalysis.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import configuration.Configuration;
import core.Entity;
import core.FilterNode;
import core.Join;
import core.OrderBy;
import core.Query;

public class Response extends Entity
{
	private String workerid;
	private int worktime;
	private int workerindex;
	private String responsesetid;

	private Map<Integer, Answer> answers = new TreeMap<>();
	
	public static Response findByWorkerId(String workerId)
	{
		Query query = new Query("response");
		query.filter("workerid", workerId);
		List<Response> results = query.run(Response.class);
		if(results.size() == 1) return results.get(0);
		
		return null;
	}
	
	public static Response findByIndex(int index)
	{
		Query query = new Query("response");
		query.filter("workerindex", index);
		List<Response> results = query.run(Response.class);
		if(results.size() == 1) return results.get(0);
		
		return null;
	}
	
	public static List<Response> retrieveCurrentResponses()
	{
		Query query = new Query("response");
		query.filter("responsesetid", Configuration.getCurrent().getValue("responsesetid"));
		query.orderBy("workerindex", OrderBy.ASC);
		return query.run(Response.class);
	}
	
	public static Query buildResponseByAnswerQuery()
	{
		Query query = new Query("response");
		query.filter("responsesetid", Responseset.getConfiguredId());
		
		Join join = new Join("response", "id", "answer", "responseid");
		query.join(join);
		
		return query;
	}
	
	public  double[] retrieveAnswerPair(Question qx, Question qy)
	{
		double[] pair = new double[2];
		pair[0] = this.retrieveAnswerValueByQuestionId(qx.getId());
		pair[1] = this.retrieveAnswerValueByQuestionId(qy.getId());
		return pair;
	}
	
	public double retrieveAnswerValueByQuestionId(String id)
	{
		Query query = new Query("answer");
		query.filter("responseid", this.id);
		query.filter("questionid", id);
		List<Answer> results = query.run(Answer.class);
		if(results.size() != 1)
		{
			System.out.println("Really?? - " + results.size());
			return -2;
		}
		else
		{
			return results.get(0).getNumericvalue();
		}
	}
	
	public static List<Response> retrieveByAnswerAndQuestion(String answer, String name)
	{
		Question question = Question.findByName(name);
		if(question == null) return new ArrayList<>();
		else
		{
			Query query = Response.buildResponseByAnswerQuery();
			FilterNode answerFilter = new FilterNode("textvalue", answer);
			FilterNode questionFilter = new FilterNode("questionid", question.getId());
			query.filter(answerFilter.and(questionFilter));
			answerFilter.setTablename("answer");
			return query.run(Response.class);
		}
	}
	
	public static List<Response> retrieveByAnswer(String answer)
	{
		Query query = Response.buildResponseByAnswerQuery();
		FilterNode answerFilter = new FilterNode("textvalue", answer);
		query.filter(answerFilter);
		answerFilter.setTablename("answer");
		return query.run(Response.class);
	}
	
	public Map<Integer, Answer> retrieveAnswers()
	{
		if(answers.isEmpty())
		{
			Query query = new Query("answer");
			FilterNode qfilter = new FilterNode("responseid", this.id);
			FilterNode pfilter = new FilterNode("parentid", "");
			query.filter(qfilter.and(pfilter));
			for(Answer answer : query.run(Answer.class))
			{
				Question question = answer.retrieveQuestion();
				answers.put(question.getQindex(), answer);
			}
		}
		return answers;
	}
	
	
	/**
	 *
	 * @return the workerid
	 */
	public String getWorkerid()
	{
		return workerid;
	}

	/**
	 *
	 * @param workerid the workerid to set
	 */
	public void setWorkerid(String workerid)
	{
		this.workerid = workerid;
	}

	/**
	 *
	 * @return the worktime
	 */
	public int getWorktime()
	{
		return worktime;
	}

	/**
	 *
	 * @param worktime the worktime to set
	 */
	public void setWorktime(int worktime)
	{
		this.worktime = worktime;
	}

	/**
	 *
	 * @return the workerindex
	 */
	public int getWorkerindex()
	{
		return workerindex;
	}

	/**
	 *
	 * @param workerindex the workerindex to set
	 */
	public void setWorkerindex(int workerindex)
	{
		this.workerindex = workerindex;
	}

	/**
	 *
	 * @return the responsesetid
	 */
	public String getResponsesetid()
	{
		return responsesetid;
	}

	/**
	 *
	 * @param responsesetid the responsesetid to set
	 */
	public void setResponsesetid(String responsesetid)
	{
		this.responsesetid = responsesetid;
	}

}
