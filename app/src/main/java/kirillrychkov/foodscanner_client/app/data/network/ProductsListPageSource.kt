package kirillrychkov.foodscanner_client.app.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kirillrychkov.foodscanner_client.app.data.PrefsStorage
import kirillrychkov.foodscanner_client.app.data.network.models.toProduct
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ProductsListPageSource(
    val apiService: ServerAPI,
    val prefsStorage: PrefsStorage
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try{
            val page: Int = params.key ?: INITIAL_PAGE_NUMBER
            val pageSize: Int = 10

            val token = "Bearer " + prefsStorage.getUser()!!.accessToken
            val response = apiService.getProducts(token)
            if (response.isSuccessful && response.body() != null){
                val result = response.body()!!.map {
                    it.toProduct()
                }
                val nextKey = if (result.size < pageSize) null else page + 1
                val prevKey = if(page == 1) null else page - 1
                return LoadResult.Page(result, prevKey, nextKey)
            }
            else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                if (errorObj.getString("error") == "Token expired!") {
                    refreshToken()
                    load(params)
                    return LoadResult.Error(Exception(errorObj.getString("Token expired!")))
                }else{
                    return LoadResult.Error(Exception(errorObj.getString("error")))
                }
            }
            else{
                return LoadResult.Error(HttpException(response))
            }

        }catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_NUMBER = 1
    }

    private suspend fun refreshToken(){
        withContext(Dispatchers.IO) {
            var refreshToken = ""
            synchronized(this){
                refreshToken = prefsStorage.getUser()!!.refreshToken
            }
            val result = apiService.refreshAccessToken(refreshToken).body()!!
            prefsStorage.refreshTokens(result)
        }
    }
}