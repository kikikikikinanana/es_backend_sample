package kr.co.shop.zconfiguration.cache.property;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.connection.RedisNode;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RedisSentinelProperty {

	private String master;
	private List<String> nodes;

	public List<RedisNode> getRedisNodes() {

		List<RedisNode> redisNodes = new ArrayList<>();

		if (nodes != null && nodes.size() > 0) {

			for (String node : nodes) {
				String[] nodeInfo = node.split("\\:");
				redisNodes.add(new RedisNode(nodeInfo[0], Integer.parseInt(nodeInfo[1])));
			}
		}

		return redisNodes;
	}
}